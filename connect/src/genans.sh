#!/bin/bash

sol=../solutions/connect_sk
prob=connect

echo "Compile solution"

g++ -O2 -Wall $sol.cpp -o x

for i in `ls ../tests/??` ; do
  echo "Test $i"
  cp $i $prob.in
  if ! (time ./x) ; then
    echo "Fail!"
    exit 1
  fi
  cp $prob.out $i.a
done

rm x
rm x.exe
rm $prob.{in,out}
