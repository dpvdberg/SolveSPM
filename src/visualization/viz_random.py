from collections import namedtuple
from enum import Enum
import matplotlib.pyplot as plt
import csv
import os
import seaborn as sns
import pandas as pd
import ntpath

setname = "Dining philosophers"
resultdir = "../test/resources/games/dining/resultsRandom"
prefix = "dining_"
indices = [2, 3, 4, 5, 6, 7]

startindex = indices[0]
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
    for index in indices:
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


        def flat_list(l):
            return [item for sublist in l for item in sublist]


        class strategies(Enum):
            IDORDER = 'IDORDER'
            REVERSED_IDORDER = 'REVERSED_IDORDER'
            OPTIMIZED_IDORDER = 'OPTIMIZED_IDORDER'
            PERMUTATION_IDORDER = 'PERMUTATION_IDORDER'
            PREDECESSOR = 'PREDECESSOR'
            METRIC = 'METRIC'
            RANDOM = 'RANDOM'


        result_dict = {}

        with open(get_result_file(index), newline='') as csvfile:
            reader = csv.reader(csvfile, delimiter=',')
            # Skip header row
            rows = [row for row in reader][1:]
            result_dict[index] = [numeric_result(Result(*row)) for row in rows]

        file_indices = list(result_dict.keys())
        results = list(result_dict.values())

        file_map = {
            i: [r for r in result_dict[i] if r.strategy == strategies.RANDOM.value] for i in file_indices
        }

        excluded_strategies = []
        strategyNames = [r for r in [r.strategy for r in results[0]] if r not in excluded_strategies]

        file_strategy_map = {
            i: {s: next(r for r in result_dict[i] if r.strategy == s) for s in strategyNames} for i in file_indices
        }

        pandaData = pd.DataFrame(
            {
                'indices': flat_list([[key] * len(l) for key, l in file_map.items()]),
                'time': flat_list([[ns_to_s(r.elapsedNs) for r in l] for key, l in file_map.items()]),
                'iterations' : flat_list([[r.iterations for r in l] for key, l in file_map.items()]),
            }
        )

        timedir = os.path.join(os.path.join(thisoutdir, 'time'), 'random')
        timedir = os.path.join(timedir, str(index))
        if not os.path.exists(timedir):
            os.makedirs(timedir)

        iterationdir = os.path.join(os.path.join(thisoutdir, 'iteration'), 'random')
        iterationdir = os.path.join(iterationdir, str(index))
        if not os.path.exists(iterationdir):
            os.makedirs(iterationdir)

        styles = ['s', 'o', 'p', 'd', 'd', 'p', 'p', 'x', 'x']

        ## PLOT TIME

        fig, ax = plt.subplots()
        ax = sns.boxplot(x="indices", y="time", data=pandaData)
        ax = sns.swarmplot(x="indices", y="time", data=pandaData, color=".2")

        ax.set_ylabel('Time (seconds)')
        ax.set_xlabel('Game index')
        plt.title("Solving time for '" + setname + "' problem set" + "\nParity Game: " + deducedShortName)
        plt.grid(True)

        fig.show()

        fig.savefig(os.path.join(timedir, deducedShortName + ".pdf"), bbox_inches='tight')

        ## PLOT ITERATIONS

        fig, ax = plt.subplots()
        ax = sns.boxplot(x="indices", y="iterations", data=pandaData)
        ax = sns.swarmplot(x="indices", y="iterations", data=pandaData, color=".2")

        ax.set_ylabel('Iteration count')
        ax.set_xlabel('Game index')
        plt.title("Iteration count for '" + setname + "' problem set" + "\nParity Game: " + deducedShortName)
        plt.grid(True)

        fig.show()

        fig.savefig(os.path.join(iterationdir, deducedShortName + ".pdf"), bbox_inches='tight')









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


    def flat_list(l):
        return [item for sublist in l for item in sublist]


    class strategies(Enum):
        IDORDER = 'IDORDER'
        REVERSED_IDORDER = 'REVERSED_IDORDER'
        OPTIMIZED_IDORDER = 'OPTIMIZED_IDORDER'
        PERMUTATION_IDORDER = 'PERMUTATION_IDORDER'
        PREDECESSOR = 'PREDECESSOR'
        METRIC = 'METRIC'
        RANDOM = 'RANDOM'


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

    file_map = {
        i: [r for r in result_dict[i] if r.strategy == strategies.RANDOM.value] for i in file_indices
    }

    excluded_strategies = []
    strategyNames = [r for r in [r.strategy for r in results[0]] if r not in excluded_strategies]

    file_strategy_map = {
        i: {s: next(r for r in result_dict[i] if r.strategy == s) for s in strategyNames} for i in file_indices
    }

    pandaData = pd.DataFrame(
        {
            'indices': flat_list([[key] * len(l) for key, l in file_map.items()]),
            'time': flat_list([[ns_to_s(r.elapsedNs) for r in l] for key, l in file_map.items()]),
            'iterations' : flat_list([[r.iterations for r in l] for key, l in file_map.items()]),
        }
    )

    timedir = os.path.join(os.path.join(thisoutdir, 'time'), 'random')
    if not os.path.exists(timedir):
        os.makedirs(timedir)

    iterationdir = os.path.join(os.path.join(thisoutdir, 'iteration'), 'random')
    if not os.path.exists(iterationdir):
        os.makedirs(iterationdir)

    styles = ['s', 'o', 'p', 'd', 'd', 'p', 'p', 'x', 'x']

    ## PLOT TIME

    fig, ax = plt.subplots()
    ax = sns.boxplot(x="indices", y="time", data=pandaData)

    ax.set_ylabel('Time (seconds)')
    ax.set_xlabel('Game index')
    plt.title("Solving time for '" + setname + "' problem set" + "\nParity Game: " + deducedShortName)
    plt.grid(True)
    plt.yscale('log')

    fig.show()

    fig.savefig(os.path.join(timedir, deducedShortName + "_all.pdf"), bbox_inches='tight')

    ## PLOT ITERATIONS

    fig, ax = plt.subplots()
    ax = sns.boxplot(x="indices", y="iterations", data=pandaData)

    ax.set_ylabel('Iteration count')
    ax.set_xlabel('Game index')
    plt.title("Iteration count for '" + setname + "' problem set" + "\nParity Game: " + deducedShortName)
    plt.grid(True)

    fig.show()

    fig.savefig(os.path.join(iterationdir, deducedShortName + "_all.pdf"), bbox_inches='tight')
