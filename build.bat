@echo off
setlocal
echo =========================================================
echo    CodeAlpha Student Grade Tracker - Build Runnable JAR
echo =========================================================

if not exist bin mkdir bin

echo 1. Compiling source files...
javac -d bin -sourcepath src/main/java src/main/java/com/codealpha/gradetracker/model/*.java src/main/java/com/codealpha/gradetracker/service/*.java src/main/java/com/codealpha/gradetracker/util/*.java src/main/java/com/codealpha/gradetracker/ui/gui/*.java src/main/java/com/codealpha/gradetracker/ui/cli/*.java src/main/java/com/codealpha/gradetracker/Main.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo 2. Packaging into executable JAR (StudentGradeTracker.jar)...
jar cfe StudentGradeTracker.jar com.codealpha.gradetracker.Main -C bin .

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] JAR Creation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo [SUCCESS] StudentGradeTracker.jar created successfully!
echo To run GUI:  java -jar StudentGradeTracker.jar
echo To run CLI:  java -jar StudentGradeTracker.jar --cli
endlocal
