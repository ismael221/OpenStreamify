$(document).ready(function () {
    $("#upload-movie-file").hide();

})



$(document).on('click', 'a[id^="upload-movie-nav"]',
        function showAndHideMenus() {
            $("#add-movie").hide(),
            $("#upload-movie-file").show()
})

$(document).on('click', 'a[id^="add-newMovie-nav"]',
    function showAndHideMenus() {
        $("#upload-movie-file").hide(),
        $("#add-movie").show()
})


var token = localStorage.getItem('access_token');

  function uploadImage(file, callback) {
    const formData = new FormData();
    formData.append('file', file);

    $.ajax({
        url: 'http://192.168.100.12:8080/api/v1/media/img/upload',  // Endpoint de upload de imagens
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        processData: false,
        contentType: false,
        data: formData,
        success: function (data) {
            callback(null, data);  // Assume que a resposta retorna o URL no campo `url`
        },
        error: function (jqXHR, textStatus, errorThrown) {
            callback(errorThrown, null);
        }
    });
}

$('#addMovieForm').submit(function (event) {
    event.preventDefault(); // Previne a recarga da página

    let title = $("#title").val();
    let synopsis = $("#synopsis").val();
    let released = $("#released").val();
    let trailerUrl = $("#trailerUrl").val();
    let backgroundImg = $("#backgroundImg")[0].files[0];
    let coverImg = $("#coverImg")[0].files[0];

    if (!title || !synopsis || !released || !backgroundImg || !coverImg || !trailerUrl) {
        alert("Favor preencher todos os campos");
        return;
    }

    // Faz o upload das imagens antes de enviar o filme
    uploadImage(backgroundImg, function (err, backgroundImgUrl) {
        if (err) {
            alert("Erro ao fazer upload da imagem de fundo");
            return;
        }

        uploadImage(coverImg, function (err, coverImgUrl) {
            if (err) {
                alert("Erro ao fazer upload da imagem da capa");
                return;
            }

            // Cria o objeto filme com os links das imagens
            let movie = {
                title: title,
                synopsis: synopsis,
                released: released,
                backgroundImgUrl: backgroundImgUrl,
                coverImgUrl: coverImgUrl,
                trailerUrl: trailerUrl,
                genres: ["ACTION", "SCIENCE_FICTION"], // Gêneros fixos para o exemplo
                type: "MOVIE"
            };

            // Envia o filme para a API
            $.ajax({
                url: 'http://192.168.100.12:8080/api/v1/movies',
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
                contentType: 'application/json',
                data: JSON.stringify(movie),
                success: function (data) {
                    console.log("Filme adicionado com sucesso:", data);
                    alert("Filme adicionado com sucesso!");
                    $("#addMovieForm")[0].reset();
                    $("#output_cover").attr("src", "");
                    $("#output_background").attr("src", "");
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("Erro ao adicionar filme:", textStatus, errorThrown);
                    alert("Ocorreu um erro ao adicionar o filme.");
                }
            });
        });
    });
});

$('#uploadMovieForm').submit(function(event){
    event.preventDefault();

    let rid = $("#ridMovie").val();
    let backgroundImg = $("#movieFile")[0].files[0];

    const formData = new FormData();
    file = backgroundImg;
    formData.append('file', file);
    formData.append('rid',rid);

    $.ajax({
        url: 'http://192.168.100.12:8080/api/v1/media/hls/upload',  // Endpoint de upload de imagens
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        processData: false,
        contentType: false,
        data: formData,
        success: function (data) {
            console.log(data);
            callback(null, data);  
        },
        error: function (jqXHR, textStatus, errorThrown) {
            callback(errorThrown, null);
        }
    });
})

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