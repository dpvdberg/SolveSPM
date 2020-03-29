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
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.types.int
import paritygame.Box
import paritygame.Diamond
import paritygame.Game
import parser.PGSolverParserNames
import java.io.File
import kotlin.random.Random

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

    private val liftingStrategyNames by option("-s", "--strategy", help = "Lifting strategy used").choice(
        StrategyName.map,
        ignoreCase = true
    ).multiple()

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

    private val randomSeed by option(
        "-rs", "--randomseed",
        help = "Fixed seed used in the '${StrategyName.RANDOM.fancyName}' lifting method, default random is used when not specified."
    ).int()

    private val seedCount by option(
        "-ra", "--randomattempts",
        help = "Number of random seeds to try in the '${StrategyName.RANDOM.fancyName}' lifting method.\nCannot be used with the '--randomseed' option"
    ).int().default(1)

    private val timer by option(
        "-t",
        "--timer",
        help = "Enable a timer for the solver"
    ).flag()

    private val benchmark by option(
        "-b",
        "--benchmark",
        help = """
            Benchmark all lifting strategies for each parity game, will automatically use timer.
            When no method is specified using the '--strategy' option, all strategies are benchmarked.
            One can also supply multiple strategies, in which case only those strategies are benchmarked.
        """.trimIndent()
    ).flag()

    private val printcsv by option(
        "-csv",
        "--commaseparated",
        help = "Print benchmark results for each parity game in comma separated format"
    ).flag()

    private val writecsv by option(
        "-o",
        "--output",
        help = "Write benchmark results for each parity game as a comma separated file in a 'results' folder"
    ).flag()

    private val iterationPrint by option(
        "-i",
        "--iteration",
        help = "Frequency to print iteration count when using verbose printing"
    ).int().default(5)

    private val pgParserName by option("-p", "--parser", help = "PGSolver parser").choice(
        PGSolverParserNames.values().associateBy { m -> m.name },
        ignoreCase = true
    ).default(PGSolverParserNames.SIMPLE)

    companion object {
        var verbose = false
        var veryVerbose = false
        var iterationPrint = 0

        var benchmarkIterations = 0
    }

    override fun run() {
        SolveSPM.verbose = verbose || veryVerbose
        SolveSPM.veryVerbose = veryVerbose
        SolveSPM.iterationPrint = iterationPrint

        if (!benchmark && (writecsv || printcsv)) {
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
        val game = pgParserName.parser().parse(file.readText())

        println("Successfully parsed parity game file.")
        printlnv(game)

        printlnvv("Nodes in game:")
        game.nodes.forEach { n -> printlnvv(n) }

        if (benchmark) {
            printlnv("Starting benchmark")

            benchmark(game, file, liftingStrategyNames)
            return
        }

        liftingStrategyNames.forEach { l -> runStrategy(game, l, randomSeed) }
    }

    private fun createSeeds() = (0 until seedCount).map { Random.Default.nextInt() }.toList()

    private fun runStrategy(
        game: Game,
        liftingStrategyName: StrategyName,
        seed: Int?
    ) {
        if (seed == null && liftingStrategyName == StrategyName.RANDOM && seedCount > 1) {
            val seeds = createSeeds()
            printlnv("Created seeds: $seeds")
            seeds.forEach { s -> runStrategy(game, liftingStrategyName, s) }
            return
        }

        val factory = LiftingStrategyFactory()
        val liftingStrategy = factory.createLiftingStrategy(
            liftingStrategyName,
            game,
            searchMethod,
            queueType,
            minMax,
            seed
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

    private fun benchmark(
        game: Game,
        file: File,
        liftingStrategyNames: List<StrategyName>
    ) {
        val factory = LiftingStrategyFactory()
        val lines = StringBuilder()
        lines.appendln("Method,ElapsedNs,Iterations,Diamond,Box")
        if (printcsv) {
            println(lines)
        }
        val liftingStrategies =
            if (liftingStrategyNames.isEmpty()) StrategyName.values().toList() else liftingStrategyNames
        println("Running ${liftingStrategies.size} strategies: $liftingStrategies.")

        val seeds = createSeeds()

        for (strategy in liftingStrategies) {
            when (strategy) {
                StrategyName.METRIC -> {
                    MinMax.values().forEach { z ->
                        lines.appendln(benchmarkSingle(factory, game, strategy, null, null, z, null))
                    }
                }
                StrategyName.PERMUTATION_IDORDER -> {
                    SearchMethod.values().forEach { z ->
                        lines.appendln(benchmarkSingle(factory, game, strategy, z, null, null, null))
                    }
                }
                StrategyName.PREDECESSOR -> {
                    QueueType.values().forEach { z ->
                        lines.appendln(benchmarkSingle(factory, game, strategy, null, z, null, null))
                    }
                }
                StrategyName.RANDOM -> {
                    seeds.forEach { z ->
                        lines.appendln(benchmarkSingle(factory, game, strategy, null, null, null, z))
                    }
                }
                else ->
                    lines.appendln(benchmarkSingle(factory, game, strategy, null, null, null, null))
            }
        }

        if (writecsv) {
            val dir = File(file.parentFile, "results")
            dir.mkdirs()
            File(dir, "${file.nameWithoutExtension}_result.csv").writeText(lines.toString())
        }
    }

    private fun benchmarkSingle(
        factory: LiftingStrategyFactory,
        game: Game,
        liftingStrategyName: StrategyName,
        searchMethod: SearchMethod?,
        queueType: QueueType?,
        minMax: MinMax?,
        seed: Int?
    ): String {
        val liftingStrategy = factory.createLiftingStrategy(
            liftingStrategyName,
            game,
            searchMethod,
            queueType,
            minMax,
            seed
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