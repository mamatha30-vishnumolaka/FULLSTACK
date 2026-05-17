@echo off
color 0a
echo ===================================================
echo     STARTING SMART CAMPUS EVENT MANAGEMENT SYSTEM
echo ===================================================
echo.
echo Cleaning old files and compiling the new files...
call mvn clean install -DskipTests

IF %ERRORLEVEL% NEQ 0 (
    color 0c
    echo.
    echo ===================================================
    echo  ERROR: MAVEN FAILED TO COMPILE THE PROJECT!
    echo  Please check the red errors above carefully.
    echo ===================================================
    pause
    exit /b
)

echo.
echo Starting the Spring Boot Server on Port 8085...
call mvn spring-boot:run

pause
