const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/chat'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    let idChat = $("#idChat").val();
    stompClient.subscribe(`/queue/chat-${idChat}`, (eventoChatMessage) => {
        showMensagem(JSON.parse(eventoChatMessage.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#mensagens").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    let idUsuario = $("#idUsuario").val();
    let mensagem = $("#mensagem").val();
    let idChat = $("#idChat").val();
    stompClient.publish({
        destination: "/app/mensagem.privada",
        body: JSON.stringify({
            'chat': {
                'id': parseInt(idChat)
            },
            'usuario': {
                'id': parseInt(idUsuario)
            },
            'mensagem': mensagem
        })
    });
}

function showMensagem(mensagem) {
    $("#mensagens").append("<tr><td>" +
        `${mensagem.dataHora} - ${mensagem.usuario.nome}: ${mensagem.mensagem}` +
        "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendMessage());
});
