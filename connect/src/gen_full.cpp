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

  printf("%d %d\n", 2 * n, n * (n - 1) + 8 * k);
  forn(t, 2)
    forn(i, n)
      forn(j, i)
        printf("+ %d %d\n", i + 1 + t * n, j + 1 + t * n);
  forn(i, k)
  {
    int a, b;
    int c, d;

    a = rndInt(n), b = rndInt(n);
    do
      c = rndInt(n);
    while (c == a);
    do
      d = rndInt(n);
    while (d == a);

    printf("+ %d %d\n", a + 1, b + n + 1);
    puts("?");
    printf("+ %d %d\n", c + 1, d + n + 1);
    puts("?");
    printf("- %d %d\n", a + 1, b + n + 1);
    puts("?");
    printf("- %d %d\n", c + 1, d + n + 1);
    puts("?");
  }
  return 0;
}
