@ECHO OFF
:: AmiCoPyJava build script. Works for both Mac OS X and Linux operating systems.
:: If you have a problem while compiling, please contact us to solve your problem.
::
:: All usage options will be described in documentation soon.
::
:: Author: Nikolay Sohryakov, nikolay.sohryakov@gmail.com

set MARIO_DIR=..\..\..\..\bin\MarioAI
set OUT_DIR="..\..\..\..\bin"
set BUILD_DIR="AmiCoBuild\PyJava"
set CMD_LINE_OPTIONS=
set AGENT=
set LIBRARY_FILE_NAME=
set COMPILE_LIBRARY="true"

set B_DIR=..\..\..\..\bin\AmiCoBuild\PyJava

:Loop
if "%1"=="" goto :Continue

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

if %COMPILE_LIBRARY% == "true" (
    if not exist "%~dp0make.cmd" (
        echo.
        echo Compile script not found. Exiting...
        echo.
        goto :eof
    )
    echo here
    call "%~dp0make.cmd"
    echo here
)

if not exist "%~dp0%B_DIR%" (
    echo.
    echo Executable file of the library not found in the path %~dp0%B_DIR%. Terminating...
    echo.
    goto :eof
)

echo.
echo Copying agents...
echo.

copy /Y "..\agents\*.py" "%~dp0%B_DIR%"

echo.
echo Copying MarioAI Benchmark files...
echo.

xcopy /E /Y /I "%~dp0%MARIO_DIR%\ch" "%~dp0%B_DIR%\ch"

cd "%~dp0%B_DIR%"

python DemoForwardJumpingAgent.py

cd %~dp0
