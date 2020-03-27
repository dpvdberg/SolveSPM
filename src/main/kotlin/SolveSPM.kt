import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import parser.PGSolverParser
import com.andreapivetta.kolor.*
import com.github.ajalt.clikt.parameters.options.default
import evaluation.SPMSolver
import evaluation.lift.LiftingStrategyFactory
import evaluation.lift.StrategyName
import util.MinMax
import util.QueueType
import util.SearchMethod
import java.util.concurrent.TimeUnit

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

fun toHMS(units : Long, base : TimeUnit = TimeUnit.NANOSECONDS) : String {
    return java.lang.String.format(
        "%02dh:%02dm:%02ds", base.toHours(units),
        base.toMinutes(units) % TimeUnit.HOURS.toMinutes(1),
        base.toSeconds(units) % TimeUnit.MINUTES.toSeconds(1)
    )
}

fun toHMSandMs(ns : Long) : String {
    return toHMS(ns, TimeUnit.NANOSECONDS) + " (${TimeUnit.NANOSECONDS.toMillis(ns)} ms)"
}

class SolveSPM : CliktCommand() {
    private val liftingStrategyName by option("-m", "--method", help = "Lifting method used").choice(
        StrategyName.map,
        ignoreCase = true
    ).required()

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

    private val queueMethod by option(
        "-qm", "--queuemethod",
        help = "Queue method used in the '${StrategyName.PREDECESSOR.fancyName}' lifting method"
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

    companion object {
        var verbose = false
        var veryVerbose = false
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

        println("Using lifting strategy: $liftingStrategyName")

        val factory = LiftingStrategyFactory()
        val liftingStrategy = factory.createLiftingStrategy(
            liftingStrategyName,
            game,
            searchMethod,
            queueMethod,
            minMax)

        printlnv("Successfully instantiated lifting strategy.")

        println("Starting solver!")

        if (timer) {
            val (partition, elapsedNs) = SPMSolver.solveTimed(game, liftingStrategy)
        } else {
            val partition = SPMSolver.solve(game, liftingStrategy)
        }

        println("SPMSolver finished.")
    }
}