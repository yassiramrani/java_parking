@echo off
echo Starting Parking System...
java -jar target\java_parking-1.0-SNAPSHOT-shaded.jar
if %errorlevel% neq 0 (
    echo Error: Java running failed. Make sure Java is installed.
    pause
)
