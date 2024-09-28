var user_id = "15013e55-78fc-4f18-892e-3d241bc3fbfc"
var token = localStorage.getItem('access_token');
var notificationList = [];

function retrieveNotifications(user_id) {
    $.ajax({
        url: 'http://192.168.100.12:8080/api/v1/notice/' + this.user_id,
        method: 'GET',
        contentType: 'application/json',
         headers: {
           'Authorization': 'Bearer ' + token
         },
        success: function (data) {
           notificationList = [];
           const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
            for (let i = 0; i < data.length; i++) {
                let notification = data[i];
                notificationList.push(notification);
                const date = new Date(notification.createdAt);
                console.log(notification)
                $("#notify").prepend('<li>' +
                '<a class="dropdown-item">'+ notification.message +'</a>'
                +
                '<p>'+months[date.getMonth()]+'<p>'
                +
                 '</li>')
            }
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

$(document).ready(function () {
    var token = localStorage.getItem('user_id');
    retrieveNotifications(this.user_id);
})

var socket = new SockJS("/ws");
      var stompClient = Stomp.over(socket);

      stompClient.connect({}, function (frame) {
        console.log("Connected: " + frame);
        stompClient.subscribe("/topic/notifications", function (message) {
          console.log(message.body);
           setTimeout(function() {
             retrieveNotifications(user_id);
           }, 3000);
        });
      });