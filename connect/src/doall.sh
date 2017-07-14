#!/bin/bash

function fail
{
  echo "Fail!"
  exit 1
}

function make
{
  echo "Compile $1"
  if ! (g++ -O2 -Wall $1.cpp -o $1) ; then
    fail
  fi
}

function nextTest
{
  ti=`printf "%02d" "$nt"`
  echo "$1 > $ti"
  $1 > ../tests/$ti || fail
  let nt=$nt+1
  let base=$base+1
}

testdir="../tests"
programs="gen_rand gen_circle gen_rand_seq gen_full gen_full2 my_cat"
nt=1
base=23917

echo "Clear enviroment"
rm -f -r $testdir
mkdir $testdir || fail

echo "Compiling programs"
for prog in $programs ; do
  make $prog
done

echo "Generate tests"

echo "Copy manual tests"
for i in `ls *.manual` ; do
  nextTest "./my_cat $i"
done

echo "Average tests"

nextTest "./gen_rand $base 10 100 500"
nextTest "./gen_rand $base 100 1000 500"
nextTest "./gen_rand_seq $base 10 100 500"
nextTest "./gen_rand_seq $base 100 1000 800"
nextTest "./gen_circle $base 100 10"

echo "Big tests"

nextTest "./gen_rand $base 300000 150000 900"
nextTest "./gen_rand_seq $base 300000 150000 900"
nextTest "./gen_circle $base 25000 3"
nextTest "./gen_circle $base 75000 1"

nextTest "./gen_full $base 370 20000"
nextTest "./gen_full2 $base 50000 70000 20000"

echo "Cleaning up"
for prog in $programs ; do
  rm -f $prog $prog.exe 2>/dev/null
done
