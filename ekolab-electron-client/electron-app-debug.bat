rmdir .\electron-src\electron-vaadin\ /s /q
xcopy .\build\install\electron-vaadin .\electron-src\electron-vaadin\ /E
xcopy .\build\resources\main\app.properties .\electron-src\ /E /Y
cd electron-src
call .\node_modules\.bin\electron .
cd ..