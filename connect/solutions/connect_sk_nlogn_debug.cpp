/**
 * Author: Sergey Kopeliovich (Burunduk30@gmail.com)
 * Date: 2013.09.08
 */

#include <cstdio>
#include <cassert>
#include <algorithm>
#include <vector>
#include <map>

using namespace std;

#define forn(i, n) for (int i = 0; i < (int)(n); i++)
#define forit(i, a) for (__typeof((a).begin()) i = (a).begin(); i != (a).end(); i++)
#define mp make_pair
#define pb push_back
#define sz(a) (int)(a).size()

//#define DEBUG
#ifdef DEBUG
#  define err(...) fprintf(stderr, __VA_ARGS__)
#else
#  define err(...) 0
#endif

#include <cstdio>
#include <string>
#include <sstream>

using namespace std;

typedef pair <int, int> pii;
ostream& operator << ( ostream &a, pii p ) { return a << "<" << p.first << "," << p.second << ">"; }

template <class T> string str( T i ) { stringstream ss; ss << i; return ss.str(); }
string str( int i ) { char s[100]; sprintf(s, "%d", i); return string(s); }

template <class T> string str( T begin, T end )
{
  stringstream ss;
  ss << "[";
  int f = 0;
  while (begin != end)
    ss << (f ? ", " : "") << *begin++, f = 1;
  ss << "]";
  return ss.str();
}
template <class T> string str( int n, T begin ) { return str(begin, begin + n); }
#define cstr(...) str(__VA_ARGS__).c_str()
#define all(a) (a).begin(), (a).end()

typedef long long ll;
typedef pair <int, int> pii;
typedef vector <int> vi;

inline int readChar()
{
  int c;
  do
    c = getchar();
  while (c <= 32);
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
  char s[16];
  int n = 0;
  while (!n || x)
    s[n++] = '0' + x % 10, x /= 10;
  while (n--)
    putchar(s[n]);
}

/* Main part */

const int N = (int)3e5;

struct edge
{
  int a, b, l, r;
};
typedef vector <edge> List;

template <class T, const int N>
struct Array
{
  T a[N];
  const char *name;
  Array( const char *name ) : name(name) { }
  T & operator[] ( int i )
  {
    if (!(0 <= i && i < N))
    {
      printf("Fail! %s[%d]", name, i);
      exit(1);
    }
    return a[i];
  }
};

Array<int, N + 1> cnt("cnt");
Array<int, N> ans("ans"), u("u"), color("color"), deg("deg");
Array<vi, N> g("g");

inline void add( int a, int b )
{
  err("add edge %d %d\n", a, b);
  g[a].pb(b), g[b].pb(a);
}

inline void dfs( int v, int value )
{
  u[v] = 1, color[v] = value;
  forn(i, sz(g[v]))
    if (!u[g[v][i]])
    {
      err("dfs: %d -> %d\n", v, g[v][i]);
      dfs(g[v][i], value);
    }
}

inline void go( int l, int r, const List &v, int vn, int add_vn ) // [l, r)
{
  err("go: [%d,%d) vn=%d add=%d v=[", l, r, vn, add_vn);
  forn(i, sz(v))
    err("(a=%d,b=%d,l=%d,r=%d)", v[i].a, v[i].b, v[i].l, v[i].r);
  err("]\n");

  if (cnt[l] == cnt[r])
    return; // no queries, only changes
  if (!sz(v))
  {
    while (l < r)
      ans[l++] = vn + add_vn;
    return;
  }  

  List v1;
  forn(i, vn)
    g[i].clear();
  forn(i, sz(v))
    if (v[i].a != v[i].b)
    {
      err("consider %d %d : [%d..%d) : ", v[i].a, v[i].b, v[i].l, v[i].r);
      if (v[i].l <= l && r <= v[i].r)
        add(v[i].a, v[i].b), err("ADD\n");
      else if (l < v[i].r && v[i].l < r)
        v1.pb(v[i]), err("continue\n");
      else
        err("-\n");
    }

  int vn1 = 0;
  forn(i, vn)
    u[i] = 0;
  forn(i, vn)
    if (!u[i])
      deg[vn1] = 0, dfs(i, vn1++);
  err("color = %s\n", cstr(vn, color));
  
  forn(i, sz(v1))
  {
    v1[i].a = color[v1[i].a];
    v1[i].b = color[v1[i].b];
    if (v1[i].a != v1[i].b)
      deg[v1[i].a]++, deg[v1[i].b]++;
  }
  err("deg = %s\n", cstr(vn, deg));

  vn = vn1, vn1 = 0;
  forn(i, vn)
    u[i] = vn1, vn1 += (deg[i] > 0), add_vn += !deg[i];
  forn(i, sz(v1))
  {
    v1[i].a = u[v1[i].a];
    v1[i].b = u[v1[i].b];
  }

  int m = (l + r) / 2; // [l, m) [m, r)
  go(l, m, v1, vn1, add_vn);
  go(m, r, v1, vn1, add_vn);
}

int main()
{
  #define NAME "connect"
  assert(freopen(NAME ".in", "r", stdin));
  assert(freopen(NAME ".out", "w", stdout));

  map <pii, int> m;
  List v;
  int n = readInt();
  int k = readInt();
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
        swap(a, b);

      if (type == '+')
        m[mp(a, b)] = i;
      else
      {
        int &j = m[mp(a, b)];
        v.pb({a, b, j, i});
        j = -1;
      }
    }
    else
      cnt[i + 1]++;
    cnt[i + 1] += cnt[i];
  }
  forit(it, m)
    if (it->second != -1)
      v.pb({it->first.first, it->first.second, it->second, k});

  go(0, k, v, n, 0);
  
  forn(i, k)
    if (cnt[i + 1] != cnt[i])
      writeInt(ans[i]), putchar('\n');
  return 0;
}
