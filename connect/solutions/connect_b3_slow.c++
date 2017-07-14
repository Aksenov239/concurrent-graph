#include <cassert>
#include <cstdio>
#include <set>

const int maxn = 300010;

int c1[maxn], c2[maxn];
std::set<int> g[maxn];

void dfs1( int vertex, int c1, int c2 ) {
  ::c1[vertex] = c1;
  ::c2[vertex] = c2;
  for (std::set<int>::iterator it = g[vertex].begin(); it != g[vertex].end(); it++)
    if (::c2[*it] != c2)
      dfs1(*it, c1, c2);
}

bool dfs2( int vertex, int target, int c ) {
  c2[vertex] = c;
  for (std::set<int>::iterator it = g[vertex].begin(); it != g[vertex].end(); it++) {
    if (*it == target)
      return true;
    if (c2[*it] != c && dfs2(*it, target, c))
      return true;
  }
  return false;
}

int main() {
  assert(freopen("connect.in", "r", stdin));
  assert(freopen("connect.out", "w", stdout));

  int n, k, a, b;
  assert(scanf("%d%d", &n, &k) == 2);
  int c1 = n, c2 = 1, ans = n;
  for (int i = 0; i < n; i++)
    ::c1[i] = i, ::c2[i] = 0;
  for (int i = 0; i < k; i++) {
    char ch;
    assert(scanf(" %s", &ch) == 1);
    if (ch == '+') {
      assert(scanf("%d%d", &a, &b) == 2), a--, b--;
      if (::c1[a] != ::c1[b])
        dfs1(a, ::c1[b], ++c2), ans--;
      g[a].insert(b);
      g[b].insert(a);
    } else if (ch == '-') {
      assert(scanf("%d%d", &a, &b) == 2), a--, b--;
      g[a].erase(b);
      g[b].erase(a);
      if (!dfs2(a, b, ++c2))
        dfs1(a, ++c1, ++c2), ans++;
    } else if (ch == '?')
      printf("%d\n", ans);
  }
  return 0;
}

