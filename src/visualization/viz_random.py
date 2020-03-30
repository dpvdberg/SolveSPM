from collections import namedtuple
from enum import Enum
import matplotlib.pyplot as plt
import csv
import os

setname = "Dining"
resultdir = "../test/resources/games/cpp/results"
filetemplate = "german_linear_{}.infinite_run_no_access_result.csv"
startindex = 2
outdir = "out"

outdir = os.path.join(outdir, setname)
deducedShortName = filetemplate[(filetemplate.index('}') + 2):][:-11]

if not os.path.exists(outdir):
    os.makedirs(outdir)


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

timedir = os.path.join(outdir, 'time')
if not os.path.exists(timedir):
    os.makedirs(timedir)

iterationdir = os.path.join(outdir, 'iteration')
if not os.path.exists(iterationdir):
    os.makedirs(iterationdir)

styles = ['s', 'o', 'p', 'd', 'd', 'p', 'p', 'x', 'x']

## PLOT TIME

fig, ax = plt.subplots()
ax.plot(file_indices, [ns_to_s(file_map[i].elapsedNs) for i in file_indices],
        marker='o', label=s,
        markersize=7
        )

ax.legend()
ax.set_ylabel('Time (seconds)')
ax.set_xlabel('Game index')
ax.set_xticks(file_indices)
plt.title("Solving time for '" + setname + "' problem set" + "\nParity Game: " + deducedShortName)
# plt.yscale('log')
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
# plt.yscale('log')
plt.grid(True)

fig.show()

fig.savefig(os.path.join(iterationdir, deducedShortName + ".pdf"), bbox_inches='tight')
