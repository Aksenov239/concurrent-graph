#!/bin/bash

echo "Compile validator"

g++ -O2 -Wall validate_sk.cpp -o x

for i in `ls ../tests/??` ; do
  echo "[$i]"
  ./x < $i
done
echo " Good! =)"

rm x
rm x.exe
