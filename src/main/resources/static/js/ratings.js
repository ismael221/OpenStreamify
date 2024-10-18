document.addEventListener("DOMContentLoaded", function () {
    const stars = document.querySelectorAll('.star');
    const submitReviewButton = document.getElementById('submitReview');
    let selectedRating = 0;

    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
    const day = String(date.getDate()).padStart(2, '0');
    const hours = date.getHours();
    const minutes = date.getMinutes();
    console.log(`${day}/${month}/${year} - ${hours}:${minutes}`); // Outputs: 28/09/2024

    // Função para colorir as estrelas até a escolhida
    function updateStars(rating) {
        stars.forEach(star => {
            if (star.getAttribute('data-value') <= rating) {
                star.classList.add('gold');
            } else {
                star.classList.remove('gold');
            }
        });
    }

    // Adiciona o evento de clique às estrelas
    stars.forEach(star => {
        star.addEventListener('click', () => {
            selectedRating = star.getAttribute('data-value');
            updateStars(selectedRating);
        });
    });

        // Simulando dados do banco de dados
    let reviewData = 0;

    

    // Adiciona o evento de clique para enviar o formulário
    submitReviewButton.addEventListener('click', () => {
        const name = document.getElementById('name').value;
        const review = document.getElementById('review').value;
        const movie = document.getElementById('movie-rid').value

        // Verifica se todos os campos estão preenchidos
        if (!name || !review || selectedRating === 0) {
            alert('Please fill out all fields and select a rating.');
            return;
        }

        // Cria o objeto de dados para enviar   name: name,
        const data = {
            user: name,
            movie: movie,
            comment: review,
            rating: selectedRating
        };

        // Faz a requisição POST para a API
        fetch('/api/v1/ratings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                alert('Review submitted successfully!');
                getRatings();
                // Aqui você pode adicionar a lógica para exibir a nova avaliação na página, se necessário
            })
            .catch(error => {
                console.error('Error:', error);
                alert('There was an error submitting your review.');
            });
    });



    // Função para gerar estrelas com base na nota
    function generateStars(rating) {
        let stars = '';
        for (let i = 0; i < 5; i++) {
            stars += i < rating ? '⭐' : '☆';
        }
        return stars;
    }



    const timestampDiv = document.createElement('div');
    timestampDiv.classList.add('timestamp');



    // Função para exibir o card
    function displayReview(review) {
        console.log("Merda pretona" +review)
        let date = new Date(review.createdAt);
        let year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
        let day = String(date.getDate()).padStart(2, '0');
        let hours = String(date.getHours()).padStart(2, '0'); // Ensures two digits
        let minutes = String(date.getMinutes()).padStart(2, '0'); // Ensures two digits
        timestampDiv.textContent = `${day}/${month}/${year} - ${hours}:${minutes}`;

        console.log(`${day}/${month}/${year} - ${hours}:${minutes}`); // Outputs: 18/10/2024 - 11:27

        const container = document.getElementById('ratings');

        const reviewCard = document.createElement('div');
        reviewCard.classList.add('review-card');

        const userName = document.createElement('h3');
        userName.textContent = review.user;

        const starsDiv = document.createElement('div');
        starsDiv.classList.add('stars');
        starsDiv.innerHTML = `<span>${generateStars(review.rating)}</span>`;

        const commentPara = document.createElement('p');
        commentPara.textContent = review.comment;

        reviewCard.appendChild(userName);
        reviewCard.appendChild(starsDiv);
        reviewCard.appendChild(commentPara);
        reviewCard.appendChild(timestampDiv);

        container.appendChild(reviewCard);
    }

    function getRatings() {
        fetch('http://192.168.100.12:8080/api/v1/ratings')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json(); // Converter a resposta para JSON
            })
            .then(data => {
                reviewData = 0;
                reviewData= data; // Adiciona os dados ao array
                console.log("Cu"+data); // Manipular os dados aqui
                for(let i=0;i<reviewData.length;i++){
                    displayReview(reviewData[i]);
                }
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
    }

    getRatings() 


    
});

