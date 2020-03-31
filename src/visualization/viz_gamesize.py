import os
import pandas as pd
import matplotlib.pyplot as plt

gameset = "Elevator2"
gamedir = "../test/resources/games/elevator"
prefix = ""
extension = ".gm"
gametemplates = [
    "elevator2",
]
indices = range(2, 6)

outdir = os.path.join('out', gameset)

nodecountmap = {'Game index' : indices}
maxpriomap = {'Game index' : indices}

styles = ['s', 'o', 'p', 'd']
matching_dict = {}
def get_matching(data, matching):
    global matching_dict

    for match in matching:
        # try to find already used matching
        if match in matching_dict and matching_dict[match] == data:
            return match

    # find new match and set
    for match in matching:
        if match not in matching_dict:
            matching_dict[match] = data
            return match

    raise Exception(
        "No remaining free matching for {} and data {}, matching dict {}".format(matching, data, matching_dict))



for gametemplate in gametemplates:
    nodecountmap[gametemplate] = []
    maxpriomap[gametemplate] = []
    for i in indices:
        linecount = 0
        maxprio = 0
        with open(os.path.join(gamedir, prefix + gametemplate + '_{}'.format(i) + extension), 'r') as f:
            first = True
            for line in f:
                if first:
                    first = False
                    continue
                maxprio = max(maxprio, int(line.split(" ")[1]))
                linecount += 1

            linecount = linecount - 1

        nodecountmap[gametemplate].append(linecount)
        maxpriomap[gametemplate].append(maxprio)

nodecountdata = pd.DataFrame(nodecountmap)
maxpriodata = pd.DataFrame(maxpriomap)

fig, ax = plt.subplots()
i = 0
for gametemplate in gametemplates:
    plt.plot('Game index', gametemplate, data=nodecountdata, marker=get_matching(gametemplate, styles))

ax.legend()
ax.set_ylabel('Node count')
ax.set_xlabel('Game index')
ax.set_xticks(indices)
plt.title("Node count for the '{}' problem set".format(gameset))
plt.grid(True)

fig.show()
fig.savefig(os.path.join(outdir, "nodecount.pdf"), bbox_inches='tight')

fig, ax = plt.subplots()
i = 0
for gametemplate in gametemplates:
    plt.plot('Game index', gametemplate, data=maxpriodata, marker=get_matching(gametemplate, styles))

ax.legend()
ax.set_ylabel('Maximum priority')
ax.set_xlabel('Game index')
ax.set_xticks(indices)
plt.title("Maximum priority for the '{}' problem set".format(gameset))
plt.grid(True)

fig.show()
fig.savefig(os.path.join(outdir, "maxprio.pdf"), bbox_inches='tight')