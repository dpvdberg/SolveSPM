import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
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
import parser.PGSolverParserNames
import java.io.File

fun main(args: Array<String>) = SolveSPM().main(args)

fun printlnv(message: Any?) {
    if (SolveSPM.verbose) {
        println("$message".yellow())
    }
}

fun printv(message: Any?) {
    if (SolveSPM.verbose) {
        print("$message".yellow())
    }
}

fun printlnvv(message: Any?) {
    if (SolveSPM.veryVerbose) {
        println("$message".cyan())
    }
}

fun printvv(message: Any?) {
    if (SolveSPM.veryVerbose) {
        print("$message".cyan())
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
    private val extension = ".gm"

    init {
        context { helpFormatter = CliktHelpFormatter(showRequiredTag = true, showDefaultValues = true) }
    }

    private val liftingStrategyName by option("-m", "--method", help = "Lifting method used").choice(
        StrategyName.map,
        ignoreCase = true
    ).default(StrategyName.METRIC)

    private val pgFile by argument(
        "Path",
        help = "A path to a parity game file in PGSolver format or a directory containing parity game files (with extension ${extension})"
    ).file(
        mustExist = true,
        mustBeReadable = true,
        canBeDir = true,
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
    ).default(QueueType.LIFO)

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
        help = "Benchmark all lifting strategies for each parity game, will automatically use timer"
    ).flag()

    private val printcsv by option(
        "-csv",
        "--commaseparated",
        help = "Print benchmark results for each parity game in comma separated format"
    ).flag()

    private val writecsv by option(
        "-o",
        "--output",
        help = "Write benchmark results for each parity game as a comma separated file"
    ).flag()

    private val pgParserName by option("-p", "--parser", help = "PGSolver parser").choice(
        PGSolverParserNames.values().associateBy { m -> m.name },
        ignoreCase = true
    ).default(PGSolverParserNames.SIMPLE)

    companion object {
        var verbose = false
        var veryVerbose = false

        var benchmarkIterations = 0
    }

    override fun run() {
        SolveSPM.verbose = verbose || veryVerbose
        SolveSPM.veryVerbose = veryVerbose

        if (!benchmark && (writecsv ||  printcsv)) {
            println("The write and print csv flags only apply for benchmarks".red())
            return
        }

        val files = mutableListOf<File>()
        if (pgFile.isFile) {
            files += pgFile
        } else if (pgFile.isDirectory) {
            files += pgFile
                .listFiles { _, fileName -> fileName.toLowerCase().endsWith(extension) }!!
        }

        files.forEach { f -> solveFile(f) }
    }

    private fun solveFile(file: File) {
        println("Reading parity game file: ${file.name}")
        val game = pgParserName.parser.parse(file.readText())

        println("Successfully parsed parity game file.")
        printlnv(game)

        printlnvv("Nodes in game:")
        game.nodes.forEach { n -> printlnvv(n) }

        if (benchmark) {
            printlnv("Starting benchmark")

            benchmark(game, file)
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
        println()
    }

    private fun benchmark(game: Game, file: File) {
        val factory = LiftingStrategyFactory()
        val lines = StringBuilder("Method,ElapsedNs,Iterations,Diamond,Box")
        if (printcsv) {
            println(lines)
        }
        for (method in StrategyName.values()) {
            if (method in strategyDependencyMap) {
                val dependencies = strategyDependencyMap[method]
                printlnvv("Lifting strategy $method depends on $strategyDependencyMap")

                when (dependencies?.get(0)) {
                    is MinMax -> {
                        dependencies.forEach { z ->
                            lines.appendln(benchmarkSingle(factory, game, method, null, null, z as MinMax))
                        }
                    }
                    is QueueType -> {
                        dependencies.forEach { z ->
                            lines.appendln(benchmarkSingle(factory, game, method, null, z as QueueType, null))
                        }
                    }
                    is SearchMethod -> {
                        dependencies.forEach { z ->
                            lines.appendln(benchmarkSingle(factory, game, method, z as SearchMethod, null, null))
                        }
                    }
                }
            } else {
                lines.appendln(benchmarkSingle(factory, game, method, null, null, null))
            }
        }

        if (writecsv) {
            File(file.parentFile, "${file.nameWithoutExtension}_result.csv").writeText(lines.toString())
        }
    }

    private fun benchmarkSingle(
        factory: LiftingStrategyFactory,
        game: Game,
        liftingStrategyName: StrategyName,
        searchMethod: SearchMethod?,
        queueType: QueueType?,
        minMax: MinMax?
    ): String {
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

        val csvline =
            "$fullName,$elapsedNs,$benchmarkIterations,${partition.getSet(Diamond).size},${partition.getSet(Box).size}"
        if (printcsv) {
            println(csvline)
        } else {
            println(
                """
            Method: $fullName
            Partition: (diamond, box) = (${partition.getSet(Diamond).size}, ${partition.getSet(Box).size})
            Execution time: ${toHMSandMs(elapsedNs)}    Iterations: $benchmarkIterations
            
            """.trimIndent()
            )
        }

        return csvline
    }
}