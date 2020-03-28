package parser

enum class PGSolverParserNames(val parser: () -> PGSolverParser) {
    GRAMMAR({GrammarPGSolverParser()}),
    SIMPLE({SimplePGSolverParser()})
}