import sys
import os
import re
from statistics import mean, stdev

def read_from_file(filename, keys):
    inf = open(filename, 'r')
    values = dict()
    for key in keys:
        values[key] = []
    for line in inf.readlines():
        ll = line.lower()
        good = None
        for key in keys:
            if key.lower() in ll:
                good = key
        if good == None:
            continue
        value = re.sub('(.*?)', '', ll).strip().split(" ")[-2]
        values[key].append(float(value))
    return values

def filename(warmup, duration, proc, size, connect, t, benchmark):
    return "out/log/w{}-d{}/{}_{}_{}_{}_{}.txt".format(warmup, duration, benchmark, proc, size, connect, t)

keys = ["throughput"]
procs = range(1, 80)

warmup = 10000
duration = 10000
sizes = [100000, 400000]
connected = [50, 80, 100]
types = ["tree"]

benchmarks=[
            "fc.FCDynamicGraph",
            "fc.FCDynamicGraphFlush",
            "lockbased.BlockingDynamicGraph",
            "lockbased.BlockingRWDynamicGraph"
           ]

directory = "out/data/w{}-d{}/".format(warmup, duration)
if not os.path.isdir(directory):
    os.makedirs(directory)

for key in keys:
    for size in sizes:
        for connect in connected:
            for t in types:
                for i in range(len(benchmarks)):
                    bench = benchmarks[i]
                    out = open((directory + "comparison_{}_{}_{}_{}_{}.dat").format(key, size, connect, t, bench.split(".")[-1]), 'w')
                    if not os.path.exists(filename(warmup, duration, 1, size, connect, t, bench)):
                        continue
                    for proc in procs:
                        if not os.path.exists(filename(warmup, duration, proc, size, connect, t, bench)):
                            continue
                        print(filename(warmup, duration, proc, size, connect, t, bench))
                        results = read_from_file(filename(warmup, duration, proc, size, connect, t, bench), keys)[key][1:]
                        out.write(str(proc) + " " + str(mean(results) / 1000 / 1000) + " " + str(stdev(results) / 1000 / 1000) + "\n")
