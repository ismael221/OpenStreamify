import { config } from './config.js';

$(document).ready(function () {
    $("form").on("submit", function (e) {
      e.preventDefault(); 
  
      const password = $("#password").val();
      const confirmPassword = $("#confirm-password").val();
      const login = $("#email").val();
  
      if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return; 
      }
  
      $.ajax({
        url: config.apiUrl + "/api/v1/auth/register",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
          login: login,
          password: password,
          role: "USER",
        }),
        success: function (response) {
          const email = $("#email").val();
          localStorage.setItem("code_verification_email", email);
  
          // Envia o e-mail de verificação
          $.ajax({
            url: config.apiUrl + "/api/v1/email/send-verification-code",
            type: "POST",
            data: { email: email },
            success: function () {
              window.location.href = "/auth/register-code";
            },
            error: function (xhr) {
              alert("Failed to send email: " + xhr.responseText);
            },
          });
        },
        error: function (xhr) {
          alert("Erro ao fazer cadastro: " + xhr.responseText);
        },
      });
    });
  });
  