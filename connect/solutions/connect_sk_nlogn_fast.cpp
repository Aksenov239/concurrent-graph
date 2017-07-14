/**
 * Author: Sergey Kopeliovich (Burunduk30@gmail.com)
 * Date: 2013.09.08 - 2013.10.01
 */

#include <cstdio>
#include <ctime>
#include <cstring>
#include <cassert>
#include <algorithm>

#define forn(i, n) for (int i = 0; i < (int)(n); i++)

typedef unsigned long long ull;

const int N = (int)3e5;
const int E = 2 * N + 1;
const int MEM = 2 * N;

struct edge
{
  int a, b, l, r;
  edge() { }
  edge( int a, int b, int l, int r ) : a(a), b(b), l(l), r(r) { }
};

int cc, cnt[N + 1], ans[N], u[N], color[N], deg[N];
int e, head[N], to[E], next[E];
edge v[N];

int mem_pos;
edge mem[MEM];

inline edge *getMem( int n )
{
  edge *res = mem + mem_pos;
  mem_pos += n;
  assert(mem_pos <= MEM);
  return res;
}

inline void add( int a, int b )
{
  next[e] = head[a], to[e] = b, head[a] = e++;
  next[e] = head[b], to[e] = a, head[b] = e++;
}

int cnt_dfs;

inline void dfs( int v, int value )
{
  cnt_dfs |= (u[v] == cc - 1);
  u[v] = cc, color[v] = value;
  for (int e = head[v]; e; e = next[e])
    if (u[to[e]] != cc)
      dfs(to[e], value);
}

inline void go( int l, int r, int vlen, edge *v, int vn, int add_vn ) // [l, r)
{
  if (cnt[l] == cnt[r])
    return; // no queries, only changes
  if (!vlen)
  {
    while (l < r)
      ans[l++] = vn + add_vn;
    return;
  }  

  int vlen1 = 0;
  e = 1, cc += 2;
  memset(head, 0, sizeof(head[0]) * vn);
  forn(i, vlen)
    if (v[i].a != v[i].b)
    {
      if (v[i].l <= l && r <= v[i].r)
        add(v[i].a, v[i].b);
      else if (l < v[i].r && v[i].l < r)
      {
        u[v[i].a] = u[v[i].b] = cc - 1;
        std::swap(v[i], v[vlen1++]);
      }
    }

  int vn1 = 0;
  forn(i, vn)
    if (u[i] != cc)
    {
      cnt_dfs = 0, dfs(i, vn1);
      cnt_dfs ? vn1++ : add_vn++;
    }
  
  int old = mem_pos;
  edge *o = getMem(vlen1);
  forn(i, vlen1)
  {
    o[i] = v[i];
    v[i].a = color[v[i].a];
    v[i].b = color[v[i].b];
  }

  int m = (l + r) >> 1; // [l, m) [m, r)
  go(l, m, vlen1, v, vn1, add_vn);
  go(m, r, vlen1, v, vn1, add_vn);

  forn(i, vlen1)
    v[i] = o[i];
  mem_pos = old;
}

inline int readChar()
{
  int c = getchar();
  while (c <= 32)
    c = getchar();
  return c;
}

inline int readInt()
{
  int x = 0, c = readChar();
  while ('0' <= c && c <= '9')
    x = x * 10 + c - '0', c = getchar();
  return x;
}

inline void writeInt( int x )
{
  char s[20];
  int n = 0;
  while (x || !n)
    s[n++] = '0' + x % 10, x /= 10;
  while (n--)
    putc(s[n], stdout);
}

template <const int max_size, class Data, const Data default_value>
struct hashTable
{
  ull hash[max_size];
  Data f[max_size];
  int size;

  inline int position( ull H ) const
  {
    int i = H % max_size;
    while (hash[i] && hash[i] != H)
      if (++i == max_size)
        i = 0;
    return i;
  }

  inline Data & operator [] ( ull H )
  {
    int i = position(H);
    if (!hash[i])
      hash[i] = H, f[i] = default_value, size++;
    return f[i];
  }
};

const int HSIZE = (int)1e6 + 3;
hashTable<HSIZE, int, -1> m;

int main()
{
  #define NAME "connect"
  assert(freopen(NAME ".in", "r", stdin));
  assert(freopen(NAME ".out", "w", stdout));

  int n = readInt();
  int k = readInt();
  int vlen = 0;
  forn(i, k)
  {
    char type = readChar();
    if (type == '+' || type == '-')
    {
      int a = readInt() - 1;
      int b = readInt() - 1;
      if (a == b) // loop
      {
        k--, i--;
        continue;
      }
      if (a > b)
        std::swap(a, b);
      ull state = ((ull)a << 20) + b + 13;

      if (type == '+')
        m[state] = i;
      else
      {
        int &j = m[state];
        v[vlen++] = edge(a, b, j, i);
        j = -1;
      }
    }
    else
      cnt[i + 1]++;
    cnt[i + 1] += cnt[i];
  }

  ull h;
  forn(it, HSIZE)
    if ((h = m.hash[it]) != 0 && m.f[it] != -1)
    {
      int a = (h - 13) >> 20;
      int b = (h - 13) & ((1 << 20) - 1);
      v[vlen++] = edge(a, b, m.f[it], k);
    }

  fprintf(stderr, "%.2f : data is read\n", (double)clock() / CLOCKS_PER_SEC);
  go(0, k, vlen, v, n, 0);
  fprintf(stderr, "%.2f : problem is solved\n", (double)clock() / CLOCKS_PER_SEC);
  
  forn(i, k)
    if (cnt[i + 1] != cnt[i])
      writeInt(ans[i]), putchar('\n');
  fprintf(stderr, "%.2f : finish\n", (double)clock() / CLOCKS_PER_SEC);
  return 0;
}
