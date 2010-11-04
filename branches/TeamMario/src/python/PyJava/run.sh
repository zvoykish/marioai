#!/bin/bash

MARIO_DIR="."
OUT_DIR="."
BUILD_DIR="AmiCoBuild"
MAKE_OUT_DIR="./build"
CMD_LINE_OPTIONS=
AGENT=
LIBRARY_FILE_NAME=

if [ "$#" -gt 0 ]; then
	until [ -z "$1"  ];
	do
		case "$1" in
		"-ch"	) shift; MARIO_DIR="$1";;
		"-out"	) shift; OUT_DIR="$1";;
		"-o"	) shift; CMD_LINE_OPTIONS="$1";;
		"-agent") shift; AGENT="$1";;
		*	) echo "Usage: [-ch path_to_ch_directory] [-out path_to_output_directory]"; exit 1;;
		esac
		shift
	done
fi

make
mkdir -p "$OUT_DIR/$BUILD_DIR"

if test "${OS}" = ""; then
	OS=`uname -s` # Retrieve the Operating System
fi
if [ "${OS}" = "Linux" ]; then
	LIBRARY_FILE_NAME="libAmiCoPyJava.so"
elif [ "${OS}" = "Darwin" ]; then
	LIBRARY_FILE_NAME="libAmiCoPyJava.dylib"
fi
	
if [ "$LIBRARY_FILE_NAME" = "" ]; then
	echo "Unknown OS type: ${OS}. Exit..."
	exit 1
fi
	
mv "$MAKE_OUT_DIR/$LIBRARY_FILE_NAME" "$OUT_DIR/$BUILD_DIR/"
rm "$MAKE_OUT_DIR"
cp AmiCoRunner.sh "$OUT_DIR/$BUILD_DIR/"
cp ../agents/*.py "$OUT_DIR/$BUILD_DIR/"
cp -r "$MARIO_DIR/ch" "$OUT_DIR/$BUILD_DIR/"

if [ "$AGENT" = "" ]; then
	echo "Incorrect input! Option '-agent <agent_file>' is necessary!"
fi

cd "$OUT_DIR/$BUILD_DIR/"
if [ "${OS}" = "Linux" ]; then
	./AmiCoRunner.sh "$AGENT" "$CMD_LINE_OPTIONS"
elif [ "${OS}" = "Darwin" ]; then
	python "$AGENT" "$CMD_LINE_OPTIONS"
fi
