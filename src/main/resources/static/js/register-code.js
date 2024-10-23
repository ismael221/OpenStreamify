import { config } from './config.js';

$("#register-code-form").on("submit", function (e) {
    e.preventDefault(); // Evita o comportamento padrão de submit

    // Obtém os dados do formulário
    var digit1 = $("#digit1").val();
    var digit2 = $("#digit2").val();
    var digit3 = $("#digit3").val();
    var digit4 = $("#digit4").val();
    var digit5 = $("#digit5").val();
    var digit6 = $("#digit6").val();

    var code = digit1 + digit2 + digit3 + digit4 + digit5 + digit6;
    var email = localStorage.getItem("code_verification_email");
    var codeString = code.toString();

    // Envia os dados via AJAX
    $.ajax({
      url: config.apiUrl +`/api/v1/verify`,
      type: "POST",
      contentType: "application/json",
      headers: {
        'Authorization': 'Bearer ' + config.apiKey
      },
      data: JSON.stringify({
        email: email,
        code: codeString,
      }),
      success: function (response) {
        setTimeout(function () {
          window.location.href = "/auth/login";
        }, 2000);
      },
      error: function (xhr, status, error) {
        console.log(error);
      },
    });
  });