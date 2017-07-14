#include <cstdio>

char s[(int)1e7];

int main( int argc, char *argv[] )
{
  freopen(argv[1], "r", stdin);
  while (gets(s))
    puts(s);
  return 0;
}
