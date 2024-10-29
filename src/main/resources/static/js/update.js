import { config } from './config.js';


$(document).ready(function () {
    $('#updatePasswordForm').on('submit', function (e) {
        e.preventDefault();

        // Captura os valores do formulário
        var email = $('#email').val();
        var newPassword = $('#newPassword').val();
        var confirmPassword = $('#confirmPassword').val();
        var userRole = "USER";

        if (newPassword !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }

        $.ajax({
            url: config.apiUrl + '/api/v1/auth/update',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                login: email,
                password: newPassword,
                role: userRole
            }),
            success: function (response) {

                alert('Password updated successfully!');
                window.location.href = '/auth/login'; // Redireciona para a página de login após a atualização
            },
            error: function (xhr, status, error) {
                // Exibe uma mensagem de erro se a requisição falhar
                alert('Failed to update password: ' + xhr.responseText);
            }
        });
    });
});