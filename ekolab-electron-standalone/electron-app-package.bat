call gradlew installDist
rmdir .\electron-src\electron-vaadin-win32-x64\ /s /q
cd electron-src
call electron-packager . --no-prune --icon=icon.ico
xcopy ..\build\install\electron-vaadin .\electron-vaadin-win32-x64\electron-vaadin\ /E
xcopy ..\build\resources\main\app.properties .\electron-vaadin-win32-x64\ /E /Y
cd ..