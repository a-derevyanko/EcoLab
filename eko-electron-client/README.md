# Десктопное приложение на Electron+Java

Настольное Java приложение с HTML 5 UI, реализованное на Electron и Vaadin

## Используемые технологии

1. Node JS
2. Electron
3. Gradle
4. JDK 8
5. Jetty HTTP Server
6. Vaadin Framework

## Функции

1. Jetty сервер с Web Sockets 
2. Vaadin UI написанный Java
3. Двустороннее взаимодействие приложения Electron и веб-приложения с помощью функций javascript
4. Автозапуск/остановка сервера при открытии/закрытии приложения

## Сборка

__Внимание:__ Пока что доступна только сборка Windows приложения.

1. Собрать java приложение:
   
        > gradlew installDist
2. Загрузить и установить `npm` from https://nodejs.org/en/download/
3. Установить необходимые `npm` модули:

        > cd electron-src
        > npm install
        > npm install electron-packager -g
        
4. Запуск в debug режиме:

        > electron-app-debug.bat
        
5. Сборка настольного приложения:

        > electron-app-package.bat

_Приложение собирается в `electron-src\electron-vaadin-win32-x64`_
