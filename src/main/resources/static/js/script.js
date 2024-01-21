$(document).ready(function (){

})

/* ################################ Filmes ################################### */
//Adicionar filme:
function adicionarFilme(filme) {
    $.ajax({
        url: 'http://localhost:8080/filme/adicionar',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(filme),
        success: function (data) {
            alert('Filme inserido com sucesso')
            console.log(data);
            $('#titulo').val('');
            titulo = $('#titulo').val('');
            sinopse = $('#sinopse').val('');
            genero = $('#genero').val('');
            anoLancamento = $('#anoLancamento').val('');
        },
        error: function () {
            alert('Não foi possível adicionar o filme.');
            console.log(this.error)
        }
    })
}
$('#formCadastroFilme').submit(function (event) {
    event.preventDefault();
    let titulo = $('#titulo').val();
    let sinopse = $('#sinopse').val();
    let genero = $('#genero').val();
    let anoLancamento = $('#anoLancamento').val();

    if (!titulo || !sinopse || !genero || !anoLancamento) {
        alert('Por favor, preencha todos os campos.');
        return;
    }
    let filme = {
        titulo: titulo,
        sinopse: sinopse,
        genero: genero,
        anoLancamento: anoLancamento
    };
    adicionarFilme(filme);
});

//Deletar Filme:
function deletarFilme(id){
    $.ajax({
        url: 'http://localhost:8080/filme/deletar/'+id,
        method: 'DELETE',
        success: function (data){
            alert("Filme removido com sucesso")
        },
        error: function (){
            console.log(this.error())
        }
    })
}

//Atualizar Filme:
function atualizarFilme(filme,id){
    $.ajax({
        url: 'http://localhost:8080/filme/atualizar/'+id,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            id: filme.id,
            titulo: filme.titulo,
            sinopse: filme.sinopse,
            genero: filme.genero,
            anoLancamento: filme.anoLancamento
        }),
        success: function (data){
            alert("Filme atualizado com sucesso");
        },
        error: function () {
            alert("Não foi possível atualizar o filme");
            console.log(this.error())
        }
    })
}

//Listar filmes:
function  carregarFilmes(){
    $.ajax({
        url: 'http://localhost:8080/filme/listar',
        method: 'GET',
        success:function (data){

        },
        error:function (){
            alert("Não foi possivel carregar os filmes, tenta novamente mais tarde");
            console.log(this.error())
        }
    })
}

/* ################################ Analises ################################### */
//Adicionar Analise:
function  adicionarAnalise(analise){
    $.ajax({
        url: 'http://localhost:8080/analise/adicionar',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(analise),
        success:function (data){
            alert("Analise adicionada com sucesso")
            console.log(data)
        },
        error: function (){
            alert("Não foi possivel adicionar a analise")
            console.log(this.error)
        }
    })
}
$('#formAdicionarAnalise').submit(function (event){
    event.preventDefault();
    let idFilme = $("#filmeId").val();
    let comment = $("#comment").val();
    let nota = $("#nota").val();

    if (!idFilme || !comment || !nota){
        alert("Favor preencher todos os campos")
        return;
    }
    let analise = {
        filme : {
            id: idFilme
        },
        comment: comment,
        nota:nota
    }
    adicionarAnalise(analise);
})

//Deletar Analise:
function deletarAnalise(id){
    $.ajax({
        url: 'http://localhost:8080/analise/deletar/'+id,
        method: 'DELETE',
        success: function (data){
            alert("Analise removida com sucesso")
        },
        error: function (){
            alert("Erro ao deletar analise");
            console.log(this.error())
        }
    })
}

//AtualizarAnalise
function atualizarAnalise(analise,idAnalise,idFilme){
    $.ajax({
        url: 'http://localhost:8080/analise/'+idAnalise,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            filme:{
                id: idFilme
            },
            comment: analise.comment,
            nota: analise.nota
        }),
        success: function (data){
            alert("Filme atualizado com sucesso")
        },
        error: function (){
            alert("Não foi possivel atualizar o filme")
        }
    })
}

//Listar Analises
function  carregarAnalises(idFilme){
    $.ajax({
        url: 'http://localhost:8080/analise/buscar/'+idFilme,
        method: 'GET',
        success: function (data){

        },
        error: function (){
            alert("Não foi possível carregar as análises, tente novamente mais tarde");
            console.log(this.error())
        }
    })
}