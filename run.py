#!/usr/bin/python3.4

import os
import sys

def filename(warmup, duration, proc, size, connect, t, benchmark):
    return "out/log/w{}-d{}/{}_{}_{}_{}_{}.txt".format(warmup, duration, benchmark, proc, size, connect, t)

classpath = "bin:lib/jmh-core-0.1.jar"
mainclass = "test.Measure"

keys = ["throughput"]

warmup = 10000
duration = 10000
iterations = 5
procs = [1, 3, 7, 14, 21, 28, 35, 42, 49, 56, 63]
sizes = [100000, 400000]
connected = [50, 80, 100]
types = ["tree", "trees"]

max_proc = int(sys.argv[1])

benchmarks = [
           "fc.FCDynamicGraph",
           "fc.FCDynamicGraphFlush",
           "fc.FCClassicDynamicGraphFlush",
           "lockbased.BlockingDynamicGraph",
           "lockbased.BlockingRWDynamicGraph",
           "sequential.SequentialDynamicGraph"
         ]

if not os.path.isdir("out/log/w{}-d{}/".format(warmup, duration)):
     os.makedirs("out/log/w{}-d{}/".format(warmup, duration))

for proc in procs:
    if proc > max_proc:
        break
    for size in sizes:
        for connect in connected:
            for t in types:
                for benchmark in benchmarks:
                    out = filename(warmup, duration, proc, size, connect, t, benchmark)
                    command = "java -server -Xmx30G -Xss256M -XX:+UseCompressedOops -cp {} {} -n {} -t {} -w {} -d {} -s {} -c {} -b {} -wt {} > {}".format(classpath, mainclass, iterations, proc, warmup, duration, size, connect, benchmark, t, out)
                    print(command)
                    os.system(command)
