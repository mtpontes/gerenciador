import { validaCampos } from './validaCampos.js';
import { usuarioRegexPatterns } from './regex/usuarioRegexPatterns.js';
document.addEventListener('DOMContentLoaded', validaLogin);

/**
 * Função de validação específica para campos de login.
 * Executa validação para os campos de login e senha, exibindo mensagens de erro conforme necessário.
 */
function validaLogin() {
    console.log("Executando validaLogin");

    const campos = [
        { input: document.getElementById('login'), regex: usuarioRegexPatterns.LOGIN_USUARIO_PATTERN, erro: document.getElementById('loginErro') },
        { input: document.getElementById('senha'), regex: usuarioRegexPatterns.SENHA_USUARIO_PATTERN, erro: document.getElementById('senhaErro') }
    ];

    validaCampos(campos);
}
