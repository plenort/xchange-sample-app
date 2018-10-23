var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#websocket").show();
    }
    else {
        $("#websocket").hide();
    }
    $("#alerts").html("");
}

function connect() {
    var socket = new SockJS('/alerts');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/alerts', function (alert) {
            showAlert(JSON.parse(alert.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showAlert(alert) {
    $("#alerts").append("<tr><td>Price of: " + alert.pair + " is below limit: " + alert.limit + ", timestamp: " + alert.timestamp + "</td></tr>");
}

function addAlert(){
    sendAlert('PUT');
}

function removeAlert(){
    sendAlert('DELETE');
}

function sendAlert($type){
    var pair = $("#pair").val();
    var limit = $("#limit").val();
    $("#form-result").attr('class','').html('');
    $.ajax({
        url: '/alert' + '?' + $.param({"pair": pair, "limit" : limit}),
        type: $type,
        success: function (data, textStatus, xhr) {
            $("#pair").val('');
            $("#limit").val('');
            $("#form-result").attr('class','text-success').html('Completed successfully.');
            updateAlerts();
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log('Error in Operation: ' + ((xhr.responseJSON.message) ? xhr.responseJSON.message : xhr));
            $("#form-result").attr('class','text-danger').html(((xhr.responseJSON.message) ? xhr.responseJSON.message : xhr));
        }
    });
}

function rowClick(row){
    var pair = $(row.cells[1]).text();
    var limit = $(row.cells[2]).text();
    $("#pair").val(pair);
    $("#limit").val(limit);
}

function updateAlerts(){
    $( "#alertsList" ).html('');

    $.get( "alert", function( data ) {
      var alertsList = '';
      alertsList += '<table class="table table-hover">';
      alertsList += '<thead><tr><th scope="col">#&nbsp;&nbsp;&nbsp;&nbsp;</th><th scope="col">Currency Pair&nbsp;&nbsp;&nbsp;&nbsp;</th><th scope="col">Limit&nbsp;&nbsp;&nbsp;&nbsp;</th><th scope="col">Timestamp</th></tr></thead>';
      $.each(data, function(i, obj) {
        alertsList += '<tr onclick="rowClick(this)"><th scope="row">'+(i+1)+'.</th><td>'+obj.pair+'</td><td>'+obj.limit+'</td><td>'+obj.timestamp+'</td></tr>';
      });
      alertsList += '</table>';

      $( "#alertsNumber" ).html( data.length );
      $( "#alertsList" ).html(alertsList);
    });
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#addAlert" ).click(function() { addAlert(); });
    $( "#removeAlert" ).click(function() { removeAlert(); });
    updateAlerts();
});