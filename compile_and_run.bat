@echo off
title CodeAlpha - Student Grade Tracker
echo ===================================================
echo   Compiling CodeAlpha Student Grade Tracker...
echo ===================================================

if not exist bin mkdir bin

javac -d bin -sourcepath src src\com\codealpha\gradetracker\Main.java src\com\codealpha\gradetracker\model\*.java src\com\codealpha\gradetracker\service\*.java src\com\codealpha\gradetracker\ui\*.java

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation completed successfully!
    echo Launching Student Grade Tracker Desktop Application...
    echo ---------------------------------------------------
    java -cp bin com.codealpha.gradetracker.Main
) else (
    echo [ERROR] Compilation failed. Please ensure JDK 8+ is installed and javac is in PATH.
    pause
)
