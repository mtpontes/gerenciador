import { validaCampos } from './validaCampos.js';
import { usuarioRegexPatterns } from './regex/usuarioRegexPatterns.js';

document.addEventListener('DOMContentLoaded', validaLogin);


function validaLogin () {
	console.log("executando validaLogin");

    const campos = [
        { input: document.getElementById('login'), regex: usuarioRegexPatterns.LOGIN_USUARIO_PATTERN, erro: document.getElementById('loginErro') },
        { input: document.getElementById('senha'), regex: usuarioRegexPatterns.SENHA_USUARIO_PATTERN, erro: document.getElementById('senhaErro') }
    ];
    
	validaCampos(campos);
}