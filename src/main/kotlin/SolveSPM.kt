import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import parser.PGSolverParser
import com.andreapivetta.kolor.*
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.options.default
import evaluation.SPMSolver
import evaluation.lift.LiftingStrategyFactory
import evaluation.lift.StrategyName
import util.MinMax
import util.QueueType
import util.SearchMethod
import java.util.concurrent.TimeUnit
import com.github.ajalt.clikt.output.CliktHelpFormatter
import evaluation.lift.strategyDependencyMap
import paritygame.Box
import paritygame.Diamond
import paritygame.Game

fun main(args: Array<String>) = SolveSPM().main(args)

fun printlnv(message: Any?) {
    if (SolveSPM.verbose) {
        println("$message".lightYellow())
    }
}

fun printv(message: Any?) {
    if (SolveSPM.verbose) {
        print("$message".lightYellow())
    }
}

fun printlnvv(message: Any?) {
    if (SolveSPM.veryVerbose) {
        println("$message".lightCyan())
    }
}

fun printvv(message: Any?) {
    if (SolveSPM.veryVerbose) {
        print("$message".lightCyan())
    }
}

fun toHMS(units: Long, base: TimeUnit = TimeUnit.NANOSECONDS): String {
    return java.lang.String.format(
        "%02dh:%02dm:%02ds", base.toHours(units),
        base.toMinutes(units) % TimeUnit.HOURS.toMinutes(1),
        base.toSeconds(units) % TimeUnit.MINUTES.toSeconds(1)
    )
}

fun toHMSandMs(ns: Long): String {
    return toHMS(ns, TimeUnit.NANOSECONDS) + " (${TimeUnit.NANOSECONDS.toMillis(ns)} ms)"
}

class SolveSPM : CliktCommand(help = "test") {
    init {
        context { helpFormatter = CliktHelpFormatter(showRequiredTag = true, showDefaultValues = true) }
    }

    private val liftingStrategyName by option("-m", "--method", help = "Lifting method used").choice(
        StrategyName.map,
        ignoreCase = true
    ).default(StrategyName.METRIC)

    private val pgFile by argument("Path", help = "The path to the parity game file in PGSolver format").file(
        mustExist = true,
        mustBeReadable = true,
        canBeDir = false,
        canBeFile = true
    )

    private val verbose by option(
        "-v",
        "--verbose",
        help = "Verbose printing, such as iteration count"
    ).flag()

    private val veryVerbose by option(
        "-vv",
        "--veryverbose",
        help = "Very verbose printing, such as the progress measure for each iteration"
    ).flag()

    private val searchMethod by option(
        "-sm", "--searchmethod",
        help = "Search method used in the '${StrategyName.PERMUTATION_IDORDER.fancyName}' lifting method"
    ).choice(
        SearchMethod.values().associateBy { m -> m.name },
        ignoreCase = true
    ).default(SearchMethod.BFS)

    private val queueType by option(
        "-qt", "--queuetype",
        help = "Queue type used in the '${StrategyName.PREDECESSOR.fancyName}' lifting method"
    ).choice(
        QueueType.values().associateBy { m -> m.name },
        ignoreCase = true
    ).default(QueueType.FIFO)

    private val minMax by option(
        "-mm", "--metricmethod",
        help = "Min or max measure metric used in the '${StrategyName.METRIC.fancyName}' lifting method"
    ).choice(
        MinMax.values().associateBy { m -> m.name },
        ignoreCase = true
    ).default(MinMax.MAX)

    private val timer by option(
        "-t",
        "--timer",
        help = "Enable a timer for the solver"
    ).flag()

    private val benchmark by option(
        "-b",
        "--benchmark",
        help = "Benchmark all lifting strategies, will automatically enable timer"
    ).flag()

    private val benchmarkcsv by option(
        "-csv",
        "--commaseparated",
        help = "Print benchmark results as comma separated file"
    ).flag()

    companion object {
        var verbose = false
        var veryVerbose = false

        var benchmarkIterations = 0
    }

    override fun run() {
        SolveSPM.verbose = verbose || veryVerbose
        SolveSPM.veryVerbose = veryVerbose

        println("Reading parity game file...")
        val game = PGSolverParser.parse(pgFile.readText())

        println("Successfully parsed parity game file.")
        printlnv(game)

        printlnvv("Nodes in game:")
        game.nodes.forEach { n -> printlnvv(n) }

        if (benchmark) {
            printlnv("Starting benchmark")

            benchmark(game)
            return
        }

        println("Using lifting strategy: $liftingStrategyName")

        val factory = LiftingStrategyFactory()
        val liftingStrategy = factory.createLiftingStrategy(
            liftingStrategyName,
            game,
            searchMethod,
            queueType,
            minMax
        )

        printlnv("Successfully instantiated lifting strategy.")

        println("Starting solver!")

        val partition =
            if (timer) {
                val (partition, elapsedNs) = SPMSolver.solveTimed(game, liftingStrategy)

                println("Execution time: ${toHMSandMs(elapsedNs)}")
                printlnv("Execution time nanoseconds: $elapsedNs")
                partition
            } else {
                SPMSolver.solve(game, liftingStrategy)
            }

        println("SPMSolver finished.")

        println(partition)
    }

    fun benchmark(game: Game) {
        val factory = LiftingStrategyFactory()
        if (benchmarkcsv) {
            println("Method,ElapsedNs,Iterations,Diamond,Box")
        }
        for (method in StrategyName.values()) {
            if (method in strategyDependencyMap) {
                val dependencies = strategyDependencyMap[method]
                printlnvv("Lifting strategy $method depends on $strategyDependencyMap")

                when (dependencies?.get(0)) {
                    is MinMax -> {
                        dependencies.forEach { z ->
                            benchmarkSingle(factory, game, method, null, null, z as MinMax)
                        }
                    }
                    is QueueType -> {
                        dependencies.forEach { z ->
                            benchmarkSingle(factory, game, method, null, z as QueueType, null)
                        }
                    }
                    is SearchMethod -> {
                        dependencies.forEach { z ->
                            benchmarkSingle(factory, game, method, z as SearchMethod, null, null)
                        }
                    }
                }
            } else {
                benchmarkSingle(factory, game, method, null, null, null)
            }
        }
    }

    private fun benchmarkSingle(
        factory: LiftingStrategyFactory,
        game: Game,
        liftingStrategyName: StrategyName,
        searchMethod: SearchMethod?,
        queueType: QueueType?,
        minMax: MinMax?
    ) {
        val liftingStrategy = factory.createLiftingStrategy(
            liftingStrategyName,
            game,
            searchMethod,
            queueType,
            minMax
        )
        benchmarkIterations = 0

        val (partition, elapsedNs) = SPMSolver.solveTimed(game, liftingStrategy)
        val fullName = "$liftingStrategyName ${searchMethod ?: ""}${queueType ?: ""}${minMax ?: ""}".trim()

        if (benchmarkcsv) {
            println("$fullName,$elapsedNs,$benchmarkIterations,${partition.getSet(Diamond).size},${partition.getSet(Box).size}")
        } else {
            println(
                """
            Method: $fullName
            Partition: (diamond, box) = (${partition.getSet(Diamond).size}, ${partition.getSet(Box).size})
            Execution time: ${toHMSandMs(elapsedNs)}    Iterations: $benchmarkIterations
            
            """.trimIndent()
            )
        }
    }
}