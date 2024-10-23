import { config } from './config.js';

$(document).ready(function () {
    $("#upload-movie-file").hide();
    $(".movie-file-container").hide();
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


  function uploadImage(file, callback) {
    const formData = new FormData();
    formData.append('file', file);

    $.ajax({
        url: config.apiUrl +'/api/v1/media/img/upload',  // Endpoint de upload de imagens
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + config.apiKey
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
                url: config.apiUrl +'/api/v1/movies',
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + config.apiKey
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
    let file = backgroundImg;
    formData.append('file', file);
    formData.append('rid',rid);

    $.ajax({
        url: config.apiUrl +'/api/v1/media/hls/upload',  // Endpoint de upload de imagens
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + config.apiKey
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


