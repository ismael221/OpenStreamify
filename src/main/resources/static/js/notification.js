import { config } from './config.js';

var user_id = localStorage.getItem("uuidUser");
var notificationList = [];

var email = localStorage.getItem('user');

if (!email) {
  getUserEmail().then(function(result) {
    email = result;
    localStorage.setItem('user', email); // Armazena o email no localStorage para chamadas futuras
    // Chame getUserID ou outras funções que dependem do email aqui, se necessário
    getUserID(email);
  }).catch(function(error) {
    console.error("Failed to fetch email:", error);
  });
} else {
  // O email já existe, então chame getUserID diretamente
  getUserID(email);
}

function getUserEmail() {
  return new Promise(function(resolve, reject) {
    $.ajax({
      url: config.apiUrl + '/api/v1/auth/oauth',
      method: 'GET',
      contentType: 'application/json',
      success: function(data) {
        resolve(data); // Retorna o email no caso de sucesso
      },
      error: function(error) {
        reject(error); // Retorna o erro no caso de falha
      }
    });
  });
}

function getUserID(user) {
  $.ajax({
    url: config.apiUrl + '/api/v1/auth/' + user.trim(),
    method: 'GET',
    contentType: 'application/json',
    success: function(data) {
      localStorage.setItem("uuidUser", data);
    },
    error: function(error) {
      console.error(error);
    }
  });
}
function retrieveNotifications(user_id) {
  $.ajax({
    url: config.apiUrl +'/api/v1/notice/' + localStorage.getItem('uuidUser'),
    method: 'GET',
    contentType: 'application/json',
    success: function (data) {
      $('#notify').empty();
      notificationList = [];
      if (data.length <= 0) {
        $("#countNot").hide();
      } else {
        $("#countNot").show();
      }

      const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
      for (let i = 0; i < data.length; i++) {
        let notification = data[i];
        notificationList.push(notification);

        const date = new Date(notification.createdAt);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
        const day = String(date.getDate()).padStart(2, '0');
        const hours = date.getHours();
        const minutes = date.getMinutes();

        $("#notify").prepend('<li><hr class="dropdown-divider"></li>').prepend('<li>' +
          '<a class="dropdown-item">' + notification.message + '</a>'
          +
          '<p class="notify-date">' + `${day}/${month}/${year} - ${hours}:${minutes}` + '<p>'
          +
          '</li>')
      }
      $("#notify").append('<button class="dropdown-item delete" href="#" th:text="#{notifications}" id="deleteNots">Delete all Notifications</button>')
      updateNotificationsAmount()

    },
    error: function () {
      alert("Não foi carregar as notificações")
    }
  })

}

function updateNotificationsAmount() {
  $('#countNot').text(notificationList.length);
}


$(document).on('click', 'li[id^="notifyButton"]',
  function showAndRideNotifications() {
    $('#notify').toggleClass("block");
  });


$(document).on('click', 'button[id^="deleteNots"]',
  function deleteNotifications() {
    $.ajax({
      url: config.apiUrl +'/api/v1/notice/'+ user_id,
      method: 'POST',
      contentType: 'application/json',
      success: function (data) {
        retrieveNotifications(user_id);
      },
      error: function (error) {
        console.log(error)
      }
    })
  });

$(document).ready(function () {
  getUserID(email);
  var token = localStorage.getItem('user_id');
  setTimeout(function () { retrieveNotifications(this.user_id); }, 3000);
})

var socket = new SockJS("/ws");
var stompClient = Stomp.over(socket);
var audio = new Audio(config.apiUrl +'/audio/notificationSound.mp3')

stompClient.connect({}, function (frame) {
  console.log("Connected: " + frame);
  stompClient.subscribe("/topic/notifications", function (message) {
    console.log(message.body);

    setTimeout(function () {
      retrieveNotifications(user_id);
      audio.play();
    }, 3000);
  });
});