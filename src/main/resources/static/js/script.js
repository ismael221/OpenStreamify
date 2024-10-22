
var loadBackgroundFile = function(event) {
    var output_background = document.getElementById('output_background');


    output_background.src = URL.createObjectURL(event.target.files[0]);
    output_background.onload = function() {
      URL.revokeObjectURL(output_background.src) // free memory
    }

}

var loadCoverFile = function(event) {
    var output_cover = document.getElementById('output_cover');
    output_cover.src = URL.createObjectURL(event.target.files[0]);
    output_cover.onload = function() {
      URL.revokeObjectURL(output_cover.src) // free memory
    }
}

var loadMovieFile = function(event) {
    var output = document.getElementById('video_here');
    output.src = URL.createObjectURL(event.target.files[0]);
    output.onload = function() {
      URL.revokeObjectURL(output.src) // free memory
    }
    var isUploaded = $("#movieFile").val();

    if(isUploaded){
        $(".movie-file-container").show();
    }else{
        $(".movie-file-container").hide();
    }
}