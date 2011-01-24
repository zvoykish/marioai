@ECHO OFF
:: AmiCoPyJava build script. Works for both Mac OS X and Linux operating systems.
:: If you have a problem while compiling, please contact us to solve your problem.
::
:: All usage options will be described in documentation soon.
::
:: Author: Nikolay Sohryakov, nikolay.sohryakov@gmail.com

set MARIO_DIR="..\..\..\..\bin"
set OUT_DIR="..\..\..\..\bin"
set BUILD_DIR="AmiCoBuild\PyJava"
set MAKE_OUT_DIR=".\build"
set CMD_LINE_OPTIONS=
set AGENT=
set LIBRARY_FILE_NAME=
set COMPILE_LIBRARY="true"

:Loop
IF "%1"=="" goto :Continue

goto :%~1

:-nc
echo -nc
set COMPILE_LIBRARY="false"
goto :next

:-ch
echo -ch
shift
set MARIO_DIR="%1"
goto :next

:-out
echo -out
shift
set OUT_DIR="%1"
goto :next

:-o
echo -o
shift
set CMD_LINE_OPTIONS="%1"
goto :next

:-ag
echo -ag
shift
set AGENT="%1"
goto :next
	
:next
shift
goto Loop
:Continue