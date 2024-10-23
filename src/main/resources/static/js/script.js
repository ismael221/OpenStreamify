var loadBackgroundFile = function (event) {
  var output_background = document.getElementById('output_background');


  output_background.src = URL.createObjectURL(event.target.files[0]);
  output_background.onload = function () {
    URL.revokeObjectURL(output_background.src) // free memory
  }

}

var loadCoverFile = function (event) {
  var output_cover = document.getElementById('output_cover');
  output_cover.src = URL.createObjectURL(event.target.files[0]);
  output_cover.onload = function () {
    URL.revokeObjectURL(output_cover.src) // free memory
  }
}

var loadMovieFile = function (event) {
  var output = document.getElementById('video_here');
  output.src = URL.createObjectURL(event.target.files[0]);
  output.onload = function () {
    URL.revokeObjectURL(output.src) // free memory
  }
  var isUploaded = $("#movieFile").val();

  if (isUploaded) {
    $(".movie-file-container").show();
  } else {
    $(".movie-file-container").hide();
  }
}

//Register-code.html
let timer;
let countdown = 60; // Set the countdown duration in seconds

function startResendTimer() {
  // Disable the button during the countdown
  document.getElementById('reset-link').disabled = true;

  // Start the countdown
  timer = setInterval(updateTimer, 1000);
}

function updateTimer() {
  const timerElement = document.getElementById('timer');

  if (countdown > 0) {
    timerElement.textContent = `in ${countdown}`;
    countdown--;
  } else {
    // Enable the button when the countdown reaches zero
    document.getElementById('resendBtn').disabled = false;
    timerElement.textContent = '';

    // Reset countdown for the next attempt
    countdown = 60;

    // Stop the timer
    clearInterval(timer);
  }
}

document.addEventListener("DOMContentLoaded", function (e) {
  startResendTimer();
  const email = localStorage.getItem("code_verification_email");
  if (email) {
    document.getElementById("email").innerHTML = email;
  } else {
    console.log("Email not found in localStorage");
  }
});

function moveToNext(currentInput, nextInputId) {
  if (currentInput.value.length >= 1) {
    document.getElementById(nextInputId).focus();
  }
}

function moveToPrev(event, prevInputId, currentInputId) {
  const currentInput = document.getElementById(currentInputId);
  if (event.key === "Backspace" && currentInput.value === "") {
    document.getElementById(prevInputId).focus();
  }
}
