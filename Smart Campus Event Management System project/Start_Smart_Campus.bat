@echo off
color 0b
title Smart Campus Server Launcher

echo ========================================================
echo     SPINNING UP SMART CAMPUS EVENT SYSTEM 
echo ========================================================
echo.
echo Please leave this black window open! This is the engine
echo running your website in the background.
echo.
echo Loading the web browser in 10 seconds...

:: Create a background task that waits 10 seconds and then opens the browser
start /B cmd /c "ping localhost -n 15 >nul & start http://localhost:8086/"

:: Launch the actual Spring Boot application using Maven
call mvn spring-boot:run

:: If this closes immediately, keep the pause so the user can read any errors
pause
