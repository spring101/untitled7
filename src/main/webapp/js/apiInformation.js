/**
 * Created by jkkjl on 2/7/14.
 */
var reader;
var balance;
var read_interval = 5000;
function startProxyReader() {
    reader = setInterval(readProxy, read_interval);
}
function startAntigateBalance() {
    balance = setInterval(readBalance, read_interval);
}
function startUserCounter(){
    counter = setInterval(readCount, read_interval);
}
function readCount() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", '/api/usercount', true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            $('div[id="users"]').text("LoggedIn: " + xhr.responseText);
        }
    };
    xhr.send(null);
}
function readBalance() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", '/api/antigate/balance', true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            $('div[id="balance"]').text("AntiGate balance: " + xhr.responseText);
        }
    };
    xhr.send(null);
}
function readProxy() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", '/api/avaliableproxies', true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            $('div[id="proxyCount"]').text("Proxy count: " + xhr.responseText);
        }
    };
    xhr.send(null);
}
function stopReadProxy() {
    clearInterval(reader);
}
function stopReadBalance() {
    clearInterval(balance);
}