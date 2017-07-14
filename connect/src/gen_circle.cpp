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
  if (argc < 4)
  {
    puts("Usage: <seed for random> <n> <k>");
    puts("[seed == 0] => Time()");
    return 0;
  }

  int seed = atoi(argv[1]);
  int n = atoi(argv[2]);
  int k = atoi(argv[3]);

  initrand(seed ? seed : Time());

  printf("%d %d\n", n, n * k * 4);
  for (int t = 1; t <= k; t++)
  {
    forn(i, n)
    {
      printf("+ %d %d\n", i + 1, (i + t) % n + 1);
      puts("?");
    }
    forn(i, n)
    {
      printf("- %d %d\n", i + 1, (i + t) % n + 1);
      puts("?");
    }
  }
  return 0;
}
