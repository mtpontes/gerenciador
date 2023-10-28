document.addEventListener('DOMContentLoaded', valida);
document.addEventListener('DOMContentLoaded', validaCampos);

    
function validaCampos(){
	const nome = document.getElementById('nome');
    const login = document.getElementById('login');
    const senha = document.getElementById('senha');
    const confirma = document.getElementById('confirma');

    let nomeErro = document.getElementById('nomeErro');
    let loginErro = document.getElementById('loginErro');
    let senhaErro = document.getElementById('senhaErro');
    let confirmaErro = document.getElementById('confirmaErro');
	
    nome.addEventListener('input', function() {
        nomeErro.style.display = 'none';
    });
    login.addEventListener('input', function() {
        loginErro.style.display = 'none';
    });
    senha.addEventListener('input', function() {
        senhaErro.style.display = 'none';
    });
    confirma.addEventListener('input', function() {
        confirmaErro.style.display = 'none';
    });
}

function valida(){
	
    const nome = document.getElementById('nome');
    const login = document.getElementById('login');
    const senha = document.getElementById('senha');
    const confirma = document.getElementById('confirma');

    let nomeErro = document.getElementById('nomeErro');
    let loginErro = document.getElementById('loginErro');
    let senhaErro = document.getElementById('senhaErro');
    let confirmaErro = document.getElementById('confirmaErro');

    const formulario = document.getElementById('formulario');
    formulario.addEventListener('submit', function(event){

        const nomeRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ']+([\s-][A-Za-zÀ-ÖØ-öø-ÿ']+)*$/;
        const loginRegex = /^[a-zA-Z0-9_.]{3,20}$/;
        const senhaRegex = /.{8,}/;

        if(!nomeRegex.test(nome.value.trim())){
            event.preventDefault();
            nomeErro.style.display = 'block';
        } else {
            nomeErro.style.display = 'none';
        }

        if(!loginRegex.test(login.value)){
			event.preventDefault();
            loginErro.style.display = 'block';
        } else {
            loginErro.style.display = 'none';
        }

        if(!senhaRegex.test(senha.value)){
			event.preventDefault();
            senhaErro.style.display = 'block';
        } else {
            senhaErro.style.display = 'none';
        }

        if (confirma.value !== senha.value || confirma.value == '') {
            event.preventDefault();
            console.log();
            // confirma.setCustomValidity('As senhas precisam ser iguais');
            confirmaErro.style.display = 'block';
        } else {
            confirmaErro.style.display = 'none';
        }
    });
}