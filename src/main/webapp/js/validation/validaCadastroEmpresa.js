import { validaCampos } from './validaCampos.js';
import { empresaRegexPatterns } from './regex/empresaRegexPatterns.js';

document.addEventListener('DOMContentLoaded', valida);

function valida() {
    const campos = [
        { input: document.getElementById('nome'), regex: empresaRegexPatterns.NOME_EMPRESA_PATTERN, erro: document.getElementById('nomeErro') },
        { input: document.getElementById('data'), regex: empresaRegexPatterns.DATA_EMPRESA_PATTERN, erro: document.getElementById('dataErro') }
    ];

    campos[1].input.addEventListener('keydown', (event) => handleKeyDown(campos[1].input, event));
    campos[1].input.addEventListener('input', () => handleInput(campos[1].input));
    campos[1].input.addEventListener('input', () => somenteNumerosEBarra(campos[1].input));

    validaCampos(campos);
}

function handleKeyDown(campo, event) {
    const valorAtual = campo.value;
    if (event.key === 'Backspace' && valorAtual.endsWith('/')) {
        // Se a última tecla pressionada foi Backspace e a barra está no final, remova a barra
        campo.value = valorAtual.substring(0, valorAtual.length - 1);
    }
}

function handleInput(campoData) {
    console.log('Evento de formatacao');

    const valorAtual = campoData.value;
    const valorNumerico = valorAtual.replace(/\D/g, '');

    if (valorNumerico.length > 8) {
        campoData.value = valorAtual.substring(0, 10);
    } else if (valorNumerico.length >= 2 && valorNumerico.length < 2 + 2) {
        campoData.value = valorNumerico.substring(0, 2) + '/' + valorNumerico.substring(2);
    } else if (valorNumerico.length >= 2 + 2 && valorNumerico.length < 8) {
        campoData.value = valorNumerico.substring(0, 2) + '/' + valorNumerico.substring(2, 4) + '/' + valorNumerico.substring(4);
    } else {
        campoData.value = valorAtual;
    }
}

function somenteNumerosEBarra(campo) {
    const regexSomenteNumerosEBarra = /[0-9/]+/g;
    campo.value = campo.value.match(regexSomenteNumerosEBarra) || '';
}

