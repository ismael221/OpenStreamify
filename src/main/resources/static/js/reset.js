import { config } from './config.js';

document
.querySelector("form")
.addEventListener("submit", function (event) {
  event.preventDefault(); // Impede o envio do formulário de forma tradicional

  const email = document.querySelector("#email").value;
  //TODO Create a function that gets the serverUrl

  fetch(
     config.apiUrl + "/api/v1/email/send-reset-email?email=" +
      encodeURIComponent(email),
    {
      method: "POST",
    }
  )
    .then((response) => {
      if (response.ok) {
        return response.json(); // Tenta interpretar a resposta como JSON
      } else {
        return response.text(); // Se não for JSON, trata como texto
      }
    })
    .then((data) => {
      alert(typeof data === "string" ? data : data.message);
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("Failed to send email");
    });
});