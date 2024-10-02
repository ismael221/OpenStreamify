var user_id;
var token = localStorage.getItem('access_token');
var email = localStorage.getItem('user');

var notificationList = [];

function getUserID(user){
    $.ajax({
        url: 'http://192.168.100.12:8080/api/v1/auth/' + user,
        method: 'GET',
        contentType: 'application/json',
        headers:{
          'Authorization': 'Bearer ' + token
        },
        success: function(data){
           user_id = data;
           localStorage.setItem("uuidUser",data);
        },
        error:function(error){
        console.error(error)
        }
    })
}
function retrieveNotifications(user_id) {
    $.ajax({
        url: 'http://192.168.100.12:8080/api/v1/notice/' + this.user_id,
        method: 'GET',
        contentType: 'application/json',
         headers: {
           'Authorization': 'Bearer ' + token
         },
        success: function (data) {
           $('#notify').empty();
           notificationList = [];
               if(data.length <= 0){
                       $("#countNot").hide();
                   }else{
                       $("#countNot").show();
                       }
           const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
            for (let i = 0; i < data.length; i++) {
                let notification = data[i];
                notificationList.push(notification);
                 if(notificationList.length > 0){
                     showAndRideNotifications();
                    }
                const date = new Date(notification.createdAt);
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
                const day = String(date.getDate()).padStart(2, '0');
                const hours = date.getHours();
                const minutes = date.getMinutes();
                console.log(`${day}/${month}/${year} - ${hours}:${minutes}`); // Outputs: 28/09/2024

                console.log(notification)
                $("#notify").prepend('<li><hr class="dropdown-divider"></li>').prepend('<li>' +
                '<a class="dropdown-item">'+ notification.message +'</a>'
                +
                '<p class="notify-date">'+`${day}/${month}/${year} - ${hours}:${minutes}`+'<p>'
                +
                 '</li>')
            }
             $("#notify").append('<li><a class="dropdown-item" href="#" th:text="#{notifications}" id="deleteNots">See all Notifications</a></li>')
            console.log(notificationList.length)
            updateNotificationsAmount()
            
        },
        error: function () {
            alert("Não foi carregar as notificações")
        }
    })

}

function updateNotificationsAmount(){
    $('#countNot').text(notificationList.length);
}
function showAndRideNotifications(){
       // Alternar a visibilidade do menu de notificações quando o botão for clicado
            $('#notifyButton').click(function(event) {
                $('#notify').toggle();   // Alterna entre mostrar e esconder
            });

            // Fechar o menu ao clicar fora dele
            $(document).click(function(event) {
                // Verifica se o clique foi fora do botão de notificações e do menu
                if (!$(event.target).closest('#notifyButton, #notify').length) {
                    $('#notify').hide();  // Esconde o menu
                }
            });
}

$(document).ready(function () {
    getUserID(email);
    var token = localStorage.getItem('user_id');
    setTimeout(function() {retrieveNotifications(this.user_id);}, 3000);

})

var socket = new SockJS("/ws");
      var stompClient = Stomp.over(socket);
      var audio = new Audio('http://192.168.100.12:8080/audio/notificationSound.mp3')

      stompClient.connect({}, function (frame) {
        console.log("Connected: " + frame);
        stompClient.subscribe("/topic/notifications", function (message) {
          console.log(message.body);

           setTimeout(function() {
             retrieveNotifications(user_id);
             audio.play();
           }, 3000);
        });
      });