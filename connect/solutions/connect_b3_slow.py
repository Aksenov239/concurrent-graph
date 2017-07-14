#!/usr/bin/env python3
# -*- coding: utf8 -*-

input = open("connect.in", "r")
output = open("connect.out", "w")

class Vertex:
  def __init__( self, color ):
    self.color, self.c, self.list = color, 0, set()

def dfs1( vertex, color, c ):
  global graph
  vertex.color, vertex.c = color, c
  for v in vertex.list:
    if graph[v].c != c:
      dfs1(graph[v], color, c)

def dfs2( vertex, target, c ):
  global graph
  vertex.c = c
  for v in vertex.list:
    if v == target:
      return True
    if graph[v].c != c and dfs2(graph[v], target, c):
      return True
  return False

n, k = [int(x) for x in input.readline().split()]
c, color, ans = 0, n, n
graph = [Vertex(i) for i in range(n)]
for i in range(k):
  line = input.readline().split()
  if line[0] == '+':
    a, b = [int(x) - 1 for x in line[1:]]
    if graph[a].color != graph[b].color:
      c += 1; dfs1(graph[b], graph[a].color, c); ans -= 1
    graph[a].list.add(b)
    graph[b].list.add(a)
  elif line[0] == '-':
    a, b = [int(x) - 1 for x in line[1:]]
    graph[a].list.remove(b)
    graph[b].list.remove(a)
    c += 1
    if not dfs2(graph[a], b, c):
      color += 1; c += 1; dfs1(graph[a], color, c); ans += 1
  elif line[0] == '?':
    print(ans, file=output)

