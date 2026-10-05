@echo off
setlocal
echo =========================================================
echo    CodeAlpha Student Grade Tracker - Interactive CLI Mode
echo =========================================================

if not exist bin mkdir bin

echo Compiling Java source files...
javac -d bin -sourcepath src/main/java src/main/java/com/codealpha/gradetracker/model/*.java src/main/java/com/codealpha/gradetracker/service/*.java src/main/java/com/codealpha/gradetracker/util/*.java src/main/java/com/codealpha/gradetracker/ui/gui/*.java src/main/java/com/codealpha/gradetracker/ui/cli/*.java src/main/java/com/codealpha/gradetracker/Main.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Starting CLI Mode...
java -cp bin com.codealpha.gradetracker.Main --cli
endlocal
