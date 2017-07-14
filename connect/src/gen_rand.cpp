#include <cstdio>
#include <cstdlib>
#include <cassert>

#include <algorithm>
#include <set>

#include "random.h"

using namespace std;

#define forn(i, n) for (int i = 0; i < (int)(n); i++)
#define mp make_pair

typedef pair <int, int> pii;

const int maxn = (int)1e6;

int en = 0, a[maxn], b[maxn];
set <pii> s;

int main( int argc, char *argv[] )
{
  if (argc < 5)
  {
    puts("Usage: <seed for random> <n> <k> <p>");
    puts("[seed == 0] => Time()");
    puts("  p - probability of Add() in 0..1000");
    return 0;
  }

  int seed = atoi(argv[1]);
  int n = atoi(argv[2]);
  int k = atoi(argv[3]);
  int p = atoi(argv[4]);

  initrand(seed ? seed : Time());

  printf("%d %d\n", n, 2 * k);
  while (k--)
  {
    if (en == n * (n - 1) / 2 || (en && rndInt(1000) >= p))
    {
      int i = rndInt(en);
      printf("- %d %d\n", a[i], b[i]);
      s.erase(mp(a[i], b[i]));
      s.erase(mp(b[i], a[i]));
      en--, a[i] = a[en], b[i] = b[en];
    }
    else
    { 
      int x, y;
      do
        x = R(1, n), y = R(1, n);
      while (x == y || s.count(mp(x, y)));
      printf("+ %d %d\n", x, y);
      s.insert(mp(x, y));
      s.insert(mp(y, x));
      a[en] = x, b[en++] = y;
    }
    puts("?");
  }
  return 0;
}
