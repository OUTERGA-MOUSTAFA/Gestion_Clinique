async function chaergerSpecialiste(){
    const url = `gestion-clinique/api/specialistes?specialiste=${specialiste}`

    const response = await fetch(url, {
        method : "GET",
        headers : {
            'Autorization' : 'Basic ' + btoa("generaliste@gmail.com:password123")
        }
    });

    afficher(response)
}

async function afficher(response){
    const pre = document.getElementById("container")

    pre.innerHTML = `<h3>status : ${response.status} </h3>`
    const data = await response.json()
    pre.innerHTML += `<p> ${JSON.stringfy(data)} </p>`
}










// async function chargerSpeicialiste(specialiste){
//     const url = `gestion-clinique/api/specialistes?specialiste=${specialiste}`

//     const response = await fetch(url, {
//         method : 'GET',
//         headers : {
//             'Authorization' : 'Basic' + btoa('generaliste@gmail.com:password123')
//         }
//     });

//     afficher(response);

// }
// async function afficher(response){
//     const pre = document.getElementById('resultat');
//     pre.textContent = 'Status: ' + response.status + '\n\n';
//     const data = await response.json();
//     pre.textContent += JSON.stringify(data, null, 2);
// }


// chargerSpeicialiste










