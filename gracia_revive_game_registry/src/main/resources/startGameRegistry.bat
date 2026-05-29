@echo off
chcp 65001 >nul
cls
setlocal

echo =======================
echo   Alege limba / Select Language
echo =======================
echo.
echo [1] Română
echo [2] English
echo.

set /p LANG=Choose / Alege: 

set "APP_LANG=en"

if "%LANG%"=="1" set "APP_LANG=ro"
if "%LANG%"=="2" set "APP_LANG=en"

cls

set "CLASSPATH=gracia_revive_game_registry.jar;lib/*"

set "MAIN_CLASS=com.graciarevive.registry.bootstrap.RegistryBootstrap"

echo =======================
echo  GraciaRevive Registry
echo =======================
echo.

java ^
 -Xms512M -Xmx1024M ^
 -Dfile.encoding=UTF-8 ^
 -Dapp.language=%APP_LANG% ^
 -Dlogback.statusListenerClass=ch.qos.logback.core.status.NopStatusListener ^
 -cp "%CLASSPATH%" ^
 %MAIN_CLASS%

echo.
echo Application stopped.
pause