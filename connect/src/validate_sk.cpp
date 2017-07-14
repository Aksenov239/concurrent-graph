#include "testlib.h"

#include <algorithm>
#include <set>

using namespace std;

#define ASSERT(a) if (!(a)) { quitf(_fail, "Asserion falied: LINE=%d, if=%s\n", __LINE__, #a); }

#define mp make_pair

typedef pair <int, int> pii;

set <pii> s;

int main(int argc, char * argv[])
{
  registerValidation();
  int n = inf.readInt(1, (int)3e5);
  inf.readSpace();
  int k = inf.readInt(0, (int)3e5);
  inf.readEoln();
  while (k--)
  {
    int c = inf.readChar();
    if (c == '+' || c == '-') 
    {
      inf.readSpace();
      int a = inf.readInt(1, n);
      inf.readSpace();
      int b = inf.readInt(1, n);
      ASSERT(a != b);
      if (a > b)
        swap(a, b);

      pii e = mp(a, b);
      if (c == '+')
      {
        ASSERT(!s.count(e));
        s.insert(e);
      }
      else if (c == '-')
      {
        ASSERT(s.count(e));
        s.erase(e);
      }
    }
    else if (c == '?')
    {
      ;
    }
    else
      quitf(_fail, "Unknown query '%c'", c);
    inf.readEoln();
  }
  inf.readEof();
  return 0;
}
