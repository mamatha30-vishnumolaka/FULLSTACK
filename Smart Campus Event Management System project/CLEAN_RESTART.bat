@echo off
color 0e
echo ===================================================
echo     FORCE RESTARTING SERVER AND APPLYING FIXES
echo ===================================================
echo.
echo 1. Finding and killing the old stuck server process...
FOR /F "tokens=5" %%T IN ('netstat -a -n -o ^| findstr :8085') DO (
    echo Killing old process with PID: %%T
    taskkill /F /PID %%T
)

echo.
echo 2. Old server successfully destroyed!
echo 3. Building the brand new code...
call mvn clean install -DskipTests

echo.
echo 4. Starting the fresh server...
call mvn spring-boot:run

pause
