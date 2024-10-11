function adicionarFilme(filme) {
    var token = localStorage.getItem('access_token');
    $.ajax({
        url: 'http://192.168.100.12:8080/api/v1/movies',
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        contentType: 'application/json',
        data: JSON.stringify(filme),
        success: function (data) {
            console.log(data)
        },
        error: function () {
            console.log(this.error)
        }
    })
  }
  
  $('#addMovieForm').submit(function (event) {
    alert("CLicado")
  
    event.preventDefault();
    let title = $("#title").val();
    let synopsis = $("#synopsis").val();
    let released = $("#released").val();
  
    if (!title || !synopsis || !released) {
        alert("Favor preencher todos os campos")
        return;
    }
    let movie = {
        title: title,
        synopsis: synopsis,
        released: released
    }
    adicionarFilme(movie);
  })