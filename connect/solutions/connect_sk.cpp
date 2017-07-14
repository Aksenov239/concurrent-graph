#include <cstdio>
#include <cassert>
#include <cstring>
#include <map>

using namespace std;

#define forn(i, n) for (int i = 0; i < (int)(n); i++)
#define mp make_pair

typedef pair <int, int> pii;

const int maxn = (int)3e5 + 10;
const int max_mem = maxn * 10;

int n, k, a[maxn], b[maxn], pa[maxn];
int p[maxn], size[maxn];
char type[maxn];
map <pii, int> ind;
int mpos, mem[maxn * 4];
int cc, u[maxn];
int sn, ss[max_mem];

inline int Set( int *x, int y )
{
  assert(sn < max_mem);
  ss[sn++] = (int)x, ss[sn++] = *x;
  return *x = y;
}

inline void Restore( int old )
{
  while (sn > old)
    sn -= 2, *((int *)(ss[sn])) = ss[sn + 1];
}

int Get( int a )
{
  return a == p[a] ? a : Set(&p[a], Get(p[a]));
}

void Join( int a, int b )
{
  if (size[a] > size[b])
    swap(a, b);
  Set(&p[a], b);
  Set(&size[b], size[b] + size[a]);
}

void Do( int L, int R, int pn, int *p, int ans )
{
  int oldMPos = mpos, oldSN = sn;
  int e, x, y, pn1 = 0, *p1 = mem + mpos;

  cc++;
  for (int i = L; i < R; i++)
    if (type[i] == '-')
      u[pa[i]] = cc;
  forn(i, pn)
    if (u[e = p[i]] == cc)
    {
      assert(mpos < maxn * 4);
      p1[pn1++] = e, mpos++;
    }
    else if (pa[e] > L && (x = Get(a[e])) != (y = Get(b[e])))
      Join(x, y), ans--;

  if (R - L == 1)
  {
    if (type[L] == '?')
      printf("%d\n", ans);
  }
  else
  {
    int M = (L + R) / 2;
    Do(L, M, pn1, p1, ans);
    for (int i = L; i < M; i++)
      if (type[i] == '+')
      {
        assert(mpos < maxn * 4);
        p1[pn1++] = i, mpos++;
      }
    Do(M, R, pn1, p1, ans);
  }
  mpos = oldMPos, Restore(oldSN);
}

int main()
{
  freopen("connect.in", "r", stdin);
  freopen("connect.out", "w", stdout);

  scanf("%d%d", &n, &k);
  forn(i, k)
  {
    pa[i] = k;
    scanf(" %c", &type[i]);
    if (type[i] != '?')
    {
      scanf("%d%d", &a[i], &b[i]), a[i]--, b[i]--;
      if (a[i] > b[i])
        swap(a[i], b[i]);
    }

    pii p = mp(a[i], b[i]);
    if (ind.count(p))
      pa[i] = ind[p], pa[pa[i]] = i, ind.erase(p);
    else
      ind[p] = i;
  }
  forn(i, n)
    p[i] = i, size[i] = 1;

  if (k)
    Do(0, k, 0, 0, n);
  return 0;
}
