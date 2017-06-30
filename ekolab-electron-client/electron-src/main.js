const {app, BrowserWindow, Menu, MenuItem} = require('electron');
const PropertiesReader = require('properties-reader');
const appConfig = PropertiesReader('app.properties');

let mainWindow = null;
let serverProcess = null;

// Provide API for web application
global.callElectronUiApi = function(args) {
    console.log('Electron called from web app with args "' + args + '"');

    if (args && args[0] === 'exit') {
        console.log('Kill server process');

        const kill = require('tree-kill');
        kill(serverProcess.pid, 'SIGTERM', function (err) {
            console.log('Server process killed');

            serverProcess = null;
            mainWindow.close();
        });
    }
};

app.on('window-all-closed', function () {
    app.quit();
});

app.on('ready', () =>{
    serverProcess = require('child_process')
        .spawn('cmd.exe', ['/c', 'electron-vaadin.bat'],
            {
                cwd: './electron-vaadin/bin'
            });

    serverProcess.stdout.on('data', function (data) {
        console.log('Server: ' + data);
    });

    console.log("Server PID: " + serverProcess.pid);

    const requestPromise = require('request-promise');
    let appUrl = `${appConfig.get('host')}:${appConfig.get('port')}/${appConfig.get('address')}`;

    const openWindow = function () {
        mainWindow = new BrowserWindow({
            title: 'EkoLab - Electron Vaadin application',
            width: 500,
            height: 768
        });

        const menuTemplate = [
            {
                label: 'File',
                submenu: [
                    {
                        label: 'Exit',
                        click: () =>{
                            //todo убрать appMenuItemTriggered
                            mainWindow.webContents.executeJavaScript("appMenuItemTriggered('Exit');");
                        }
                    }
                ]
            },
            {
                label: 'About',
                click: () =>{
                    //todo убрать appMenuItemTriggered
                    mainWindow.webContents.executeJavaScript("appMenuItemTriggered('About');");
                }
            },
            {
                label: 'Обновить страницу',
                click: () =>{
                    mainWindow.webContents.executeJavaScript("location.reload();");
                }
            }
        ];
        const menu = Menu.buildFromTemplate(menuTemplate);
        mainWindow.setMenu(menu);
        mainWindow.loadURL(appUrl);

        // uncomment to show debug tools
        // mainWindow.webContents.openDevTools();

        mainWindow.on('closed', function () {
            mainWindow = null;
        });

        mainWindow.on('close', function (e) {
            if (serverProcess) {
                e.preventDefault();

                mainWindow.webContents.executeJavaScript("appWindowExit();");
            }
        });
    };

    const startUp = function () {
        requestPromise(appUrl)
            .then(function (htmlString) {
                console.log('Server started!');
                openWindow();
            })
            .catch(function (err) {
                console.log('Waiting for the server start...');
                startUp();
            });
    };

    startUp();
});