import { validaCampos } from './validaCampos.js';
document.addEventListener('DOMContentLoaded', validaLogin);


function validaLogin () {
	console.log("executando validaLogin");

    const campos = [
        { input: document.getElementById('login'), regex: /^[a-zA-Z0-9_.]{3,20}$/, erro: document.getElementById('loginErro') },
        { input: document.getElementById('senha'), regex: /.{8,}/, erro: document.getElementById('senhaErro') }
    ];
    
	validaCampos(campos);
}