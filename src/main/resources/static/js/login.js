import { config } from './config.js';

$(document).ready(function () {

  $("#login-form").on("submit", function (e) {
    e.preventDefault(); 

    var email = $('input[name="login"]').val();
    var password = $('input[name="password"]').val();

    $.ajax({
      url: `${config.apiUrl}/api/v1/auth/login`,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        login: email,
        password: password,
      }),
      success: function (response) {
        // Sucesso no login
        localStorage.setItem("access_token", response.token);
        localStorage.setItem("user", email);
        // Exibe o modal de sucesso
        $("#success-modal").modal();

        setTimeout(function () {
          window.location.href = "/";
        }, 2000);
      },
      error: function (xhr, status, error) {
        // Exibe o modal de erro
        $("#error-modal").modal();
      },
    });
  });
});