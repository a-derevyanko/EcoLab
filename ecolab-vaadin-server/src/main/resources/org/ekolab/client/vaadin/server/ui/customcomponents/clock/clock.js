var rpcProxy;
var clockContainer = null;

var months = ['January', 'February', 'March', 'April', 'May', 'June', 'Jully', 'August', 'September', 'October', 'November', 'December'];
var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
var ruMonth = ['January', 'February', 'March', 'April', 'May', 'June', 'Jully', 'August', 'September', 'October', 'November', 'December'];
var ruDays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

var locale = 'en';

window.org_ecolab_client_vaadin_server_ui_customcomponents_clock_Clock = function () {
    this.onStateChange = function () {
        //var clockContainer;
        if (this.getElement().childNodes.length === 0) {
            clockContainer = document.createElement('clock');
            clockContainer.setAttribute('class', 'clock_container');
            this.getElement().appendChild(clockContainer);
        }
        locale = window.navigator.userLanguage || window.navigator.language;

        rpcProxy = this.getRpcProxy();

        setInterval('dateTime();', '1000');
    }
};

function dateTime() {
    var date = new Date;
    var year = date.getFullYear();
    var month = date.getMonth();
    var d = date.getDate();
    var day = date.getDay();
    var h = date.getHours();
    if (h < 10) {
        h = "0" + h;
    }
    var m = date.getMinutes();
    if (m < 10) {
        m = "0" + m;
    }
    var s = date.getSeconds();
    if (s < 10) {
        s = "0" + s;
    }
    clockContainer.innerHTML = '' + days[day] + ' ' + months[month] + ' ' + d + ' ' + year + ' ' + h + ':' + m + ':' + s ;
}
