$(document).ready(function () {
    carregarFilmes();
})

/* ################################ Filmes ################################### */
//Adicionar filme:
function adicionarFilme(filme) {
    $.ajax({
        url: 'https://localhost:443/api/v1/filme/adicionar',
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
function deletarFilme(id) {
    $.ajax({
        url: 'https://localhost:443/api/v1/filme/deletar/' + id,
        method: 'DELETE',
        success: function (data) {
            alert("Filme removido com sucesso")
            carregarFilmes();
        },
        error: function () {
            console.log(this.error())
        }
    })
}

//Atualizar Filme:
function atualizarFilme(id, filme) {
    $.ajax({
        url: 'https://localhost:443/api/v1/filme/atualizar/' + id,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(filme),  // Use the entire 'filme' object as the request payload
        success: function (data) {
            alert("Filme atualizado com sucesso");
            carregarFilmes();
        },
        error: function (data) {
            alert("Não foi possível atualizar o filme");
            console.log(data);
        }
    });
}


//Listar filmes:
function carregarFilmes() {
    $.ajax({
        url: 'https://localhost:443/api/v1/filme/listar',
        method: 'GET',
        success: function (data) {
            $('#tabelaFilmes').empty();
            // Adiciona cada tarefa à tabela
            for (let i = 0; i < data.length; i++) {
                let filme = data[i];

                let id = $('<td>').text(filme.id);

                let titulo = $('<td>').append(
                    $('<input>').attr('type', 'text').val(filme.titulo)
                        .addClass('form-control')
                );

                let sinopse = $('<td>').append(
                    $('<input>').attr('type', 'text').val(filme.sinopse)
                        .addClass('form-control')
                );

                let genero = $('<td>').append(
                    $('<input>').attr('type', 'text').val(filme.genero)
                        .addClass('form-control')
                );

                let anoLancamento = $('<td>').append(
                    $('<input>').attr('type', 'text').val(filme.anoLancamento)
                        .addClass('form-control')
                );

                let botaoDeletar = $('<button>')
                    .text('Excluir')
                    .addClass('btn btn-danger')
                    .click(function () {
                        deletarFilme($(this).parent().parent().attr('data-id'));
                    });
                let botaoAtualizar = $('<button>')
                    .text('Atualizar')
                    .addClass('btn btn-success')
                    .click(function () {
                        let updatedTitulo = $(this).parent().siblings("td:eq(1)").find("input").val();
                        let updatedSinopse = $(this).parent().siblings("td:eq(2)").find("input").val();
                        let updatedGenero = $(this).parent().siblings("td:eq(3)").find("input").val();
                        let updatedAnoLancamento = $(this).parent().siblings("td:eq(4)").find("input").val();
                        atualizarFilme($(this).parent().parent().attr('data-id'), {
                            id: filme.id,
                            titulo: updatedTitulo,
                            sinopse: updatedSinopse,
                            genero: updatedGenero,
                            anoLancamento: updatedAnoLancamento,
                        });
                    });

                let botaoAnalise = $('<button>')
                    .text('Detalhes')
                    .addClass('btn btn-info')
                    .click(function () {
                        console.log(filme.id);
                        window.location.href = 'https://localhost:443/exibirAnalise/' + filme.id;
                    });
                let botaoAssistir = $('<button>')
                    .text('Assistir')
                    .addClass('btn btn-info')
                    .click(function () {
                        console.log(filme.rid);
                        window.location.href = 'https://localhost:443/assistir?filme=' + filme.rid;
                    });

                let excluir = $('<td>').append(botaoDeletar);
                let atualizar = $('<td>').append(botaoAtualizar)
                let analise = $('<td>').append(botaoAnalise)
                let assistir = $('<td>').append(botaoAssistir)

                let tr = $('<tr>')
                    .attr('data-id', filme.id)
                    .append(id)
                    .append(titulo)
                    .append(sinopse)
                    .append(genero)
                    .append(anoLancamento)
                    .append(atualizar)
                    .append(excluir)
                    .append(analise)
                    .append(assistir);

                $('#tabelaFilmes').append(tr);
            }

        },
        error: function () {
            alert("Não foi possivel carregar os filmes, tenta novamente mais tarde");
            console.log(this.error())
        }
    })
}

/* ################################ Analises ################################### */
//Adicionar Analise:
function adicionarAnalise(analise) {
    $.ajax({
        url: 'https://localhost:443/api/v1/analise/adicionar',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(analise),
        success: function (data) {
            alert("Analise adicionada com sucesso")
            console.log(data)
            window.location = window.location.pathname;
        },
        error: function () {
            alert("Não foi possivel adicionar a analise")
            console.log(this.error)
        }
    })
}
$('#formAdicionarAnalise').submit(function (event) {
    event.preventDefault();
    let idFilme = $("#filmeId").val();
    let comment = $("#comment").val();
    let nota = $("#nota").val();

    if (!idFilme || !comment || !nota) {
        alert("Favor preencher todos os campos")
        return;
    }
    let analise = {
        filme: {
            id: idFilme
        },
        comment: comment,
        nota: nota
    }
    adicionarAnalise(analise);
})

//Deletar Analise:
function deletarAnalise(id) {
    $.ajax({
        url: 'https://localhost:443/analise/deletar/' + id,
        method: 'DELETE',
        success: function (data) {
            alert("Analise removida com sucesso")
        },
        error: function () {
            alert("Erro ao deletar analise");
            console.log(this.error())
        }
    })
}

//AtualizarAnalise
function atualizarAnalise(analise, idAnalise, idFilme) {
    $.ajax({
        url: 'https://localhost:443/analise/' + idAnalise,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            filme: {
                id: idFilme
            },
            comment: analise.comment,
            nota: analise.nota
        }),
        success: function (data) {
            alert("Filme atualizado com sucesso")
        },
        error: function () {
            alert("Não foi possivel atualizar o filme")
        }
    })
}

function makeAuthenticatedRequest() {
    var token = localStorage.getItem('authToken');
    if (token) {
        $.ajax({
            url: 'http://localhost:443/',
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                // Processa a resposta
            },
            error: function (xhr, status, error) {
                // Processa o erro
            }
        });
    } else {
        alert('Usuário não está autenticado.');
    }
}


