let id = id => document.getElementById(id);
let toggle = (elementId, enabled) => id(elementId).style.display = enabled ? "block" : "none";

id("connect").addEventListener("click", () => connect());
id("rock").addEventListener("click", () => sendChoice("ROCK"));
id("paper").addEventListener("click", () => sendChoice("PAPER"));
id("scissors").addEventListener("click", () => sendChoice("SCISSORS"));

function connect() {
    toggle("nameControls", false);
    setMessage("Connecting");
    var name = id("name").value;
    sessionStorage.setItem("name", name);
    doRequest("game?name=" + name, handleConnectResponse)
}

function sendChoice(choice) {
    toggleGame(false);
    setMessage("Awaiting response from opponent");
    var name = sessionStorage.getItem("name");
    var gameId = sessionStorage.getItem("gameId");
    doRequest("game/" + gameId +  "/play?name=" + name + "&selection=" + choice, handlePlayResponse)
}

function doRequest(url, callback) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      callback(this);
    }
  };
  xhttp.open("GET", url, true);
  xhttp.send();
}

function handleConnectResponse(xhttp) {
    setMessage("");

    let data = JSON.parse(xhttp.responseText);

    sessionStorage.setItem("gameId", data.gameId);
    toggleGame(true);
}

function handlePlayResponse(xhttp) {
    let data = JSON.parse(xhttp.responseText);

    setMessage("Result: " + data.outcome);
    toggleGame(true);
}

function toggleGame(enabled) {
    toggle("gameControls", enabled);
}

function setMessage(msg) {
    id("message").innerText = msg;
}