@echo off
title CodeAlpha - Student Grade Tracker (Console CLI)
if not exist bin mkdir bin
javac -d bin -sourcepath src src\com\codealpha\gradetracker\Main.java
if %ERRORLEVEL% EQU 0 (
    java -cp bin com.codealpha.gradetracker.Main --console
) else (
    echo [ERROR] Compilation failed.
    pause
)
