#!/usr/bin/env python3
# -*- coding: utf8 -*-

import testlib

limit = testlib.Limit.interval
ensure = testlib.Error.ensure
LIMIT_N = limit(1, 300000)
LIMIT_K = limit(0, 300000)
LIMIT_C = testlib.Limit.list(['?','+','-'])

def validate( inf ):
  n, k = inf.read("%d %d\n", (LIMIT_N, LIMIT_K))
  graph = set()
  for i in range(k):
    command = inf.readChar(LIMIT_C);
    if command == '?':
      inf.readEoln()
    elif command == '+':
      a, b = inf.read(" %d %d\n", (limit(1, n), limit(1, n)))
      a, b = min(a, b) - 1, max(a, b) - 1
      ensure(a != b and (a, b) not in graph, "cannot add edge (%d,%d)" % (a + 1, b + 1))
      graph.add((a, b))
    elif command == '-':
      a, b = inf.read(" %d %d\n", (limit(1, n), limit(1, n)))
      a, b = min(a, b) - 1, max(a, b) - 1
      ensure((a, b) in graph, "cannot remove edge (%d,%d)" % (a + 1, b + 1))
      graph.remove((a, b))
  inf.close();

testlib.validator(validate)

