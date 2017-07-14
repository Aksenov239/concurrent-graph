@echo off

for %%i in (../tests/??) do (
  echo "[%%i]"
  python validate.py < ../tests/%%i || exit
)
echo " Good! =)"

rm x
rm x.exe
