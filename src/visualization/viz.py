from collections import namedtuple
from enum import Enum
import matplotlib.pyplot as plt
import csv
import os
import ntpath

setname = "Elevator2"
resultdir = "../test/resources/games/elevator/results"
prefix = "elevator2_"
startindex = 2
outdir = "out"


def deduce_file_templates():
    files = [ntpath.basename(f) for f in os.listdir(resultdir) if
             os.path.isfile(os.path.join(resultdir, f)) and
             ntpath.basename(os.path.join(resultdir, f)).startswith(prefix + str(startindex))
             ]
    files = [prefix + "{}" + f[(len(prefix) + 1):] for f in files]

    return files


filetemplates = deduce_file_templates()

for filetemplate in filetemplates:
    thisoutdir = os.path.join(outdir, setname)
    deducedShortName = filetemplate[(filetemplate.index('}') + 2):][:-11]

    if not os.path.exists(thisoutdir):
        os.makedirs(thisoutdir)


    def get_result_file(index):
        return os.path.join(resultdir, filetemplate.format(index))


    def ns_to_s(ns):
        return ns / 1_000_000_000.0


    Result = namedtuple('Result', 'strategy elapsedNs iterations diamondSize boxSize')


    def numeric_result(r):
        return Result(r.strategy, int(r.elapsedNs), int(r.iterations), int(r.diamondSize), int(r.boxSize))


    class strategies(Enum):
        IDORDER = 'IDORDER'
        REVERSED_IDORDER = 'REVERSED_IDORDER'
        OPTIMIZED_IDORDER = 'OPTIMIZED_IDORDER'
        PERMUTATION_IDORDER = 'PERMUTATION_IDORDER'
        PREDECESSOR = 'PREDECESSOR'
        METRIC = 'METRIC'
        RANDOM = 'RANDOM'


    excluded_strategies = [strategies.RANDOM]

    excluded_strategies = [s.name for s in excluded_strategies]

    result_dict = {}

    currentIndex = startindex
    while os.path.isfile(get_result_file(currentIndex)):
        with open(get_result_file(currentIndex), newline='') as csvfile:
            reader = csv.reader(csvfile, delimiter=',')
            # Skip header row
            rows = [row for row in reader][1:]
            result_dict[currentIndex] = [numeric_result(Result(*row)) for row in rows]

        currentIndex = currentIndex + 1

    file_indices = list(result_dict.keys())
    results = list(result_dict.values())
    strategies = [r for r in [r.strategy for r in results[0]] if r not in excluded_strategies]

    file_strategy_map = {
        i: {s: next(r for r in result_dict[i] if r.strategy == s) for s in strategies} for i in file_indices
    }

    timedir = os.path.join(thisoutdir, 'time')
    if not os.path.exists(timedir):
        os.makedirs(timedir)

    iterationdir = os.path.join(thisoutdir, 'iteration')
    if not os.path.exists(iterationdir):
        os.makedirs(iterationdir)

    styles = ['s', 'o', 'p', 'd', 'd', 'p', 'p', 'x', 'x']

    ## PLOT TIME

    fig, ax = plt.subplots()
    i = 0
    for s in strategies:
        ax.plot(file_indices, [ns_to_s(file_strategy_map[i][s].elapsedNs) for i in file_indices],
                marker=styles[i], label=s,
                markersize=7
                )
        i = i + 1

    ax.legend()
    ax.set_ylabel('Time (seconds)')
    ax.set_xlabel('Game index')
    ax.set_xticks(file_indices)
    plt.title("Solving time (logarithmic) for '" + setname + "' problem set" + "\nParity Game: " + deducedShortName)
    plt.yscale('log')
    plt.grid(True)

    fig.show()

    fig.savefig(os.path.join(timedir, deducedShortName + ".pdf"), bbox_inches='tight')

    ## PLOT ITERATION

    fig, ax = plt.subplots()
    i = 0
    for s in strategies:
        ax.plot(file_indices, [file_strategy_map[i][s].iterations for i in file_indices],
                marker=styles[i], label=s,
                markersize=7
                )
        i = i + 1

    ax.legend()
    ax.set_ylabel('Iteration count')
    ax.set_xlabel('Game index')
    ax.set_xticks(file_indices)
    plt.title("Iteration count for '" + setname + "' problem set" + "\nParity Game: " + deducedShortName)
    plt.grid(True)

    fig.show()

    fig.savefig(os.path.join(iterationdir, deducedShortName + ".pdf"), bbox_inches='tight')
