import { validaCampos } from './validaCampos.js';
import { empresaRegexPatterns } from './regex/empresaRegexPatterns.js';

document.addEventListener('DOMContentLoaded', valida);

/**
 * Validação de campos de entrada para o formulário.
 * Os campos são definidos como objetos, cada um contendo o input associado,
 * a expressão regular de validação e a área de exibição de erro correspondente.
 * A validação ocorre durante a interação do usuário e fornece feedback instantâneo.
 */
function valida() {
    const campos = [
        { input: document.getElementById('nome'), regex: empresaRegexPatterns.NOME_EMPRESA_PATTERN, erro: document.getElementById('nomeErro') },
        { input: document.getElementById('data'), regex: empresaRegexPatterns.DATA_EMPRESA_PATTERN, erro: document.getElementById('dataErro') }
    ];

    // Adiciona listeners de eventos para validação em tempo real
    campos[1].input.addEventListener('keydown', (event) => handleKeyDown(campos[1].input, event));
    campos[1].input.addEventListener('input', () => handleInput(campos[1].input));
    campos[1].input.addEventListener('input', () => somenteNumerosEBarra(campos[1].input));

    // Inicia a validação dos campos
    validaCampos(campos);
}

/**
 * Manipula o evento de pressionar uma tecla para remover a barra '/' no final do valor do campo.
 * Se a tecla pressionada for 'Backspace' e a barra estiver no final, remove a barra do valor atual.
 * 
 * @param {HTMLInputElement} campo - Elemento input do formulário.
 * @param {KeyboardEvent} event - Objeto de evento associado à ação de pressionar uma tecla.
 */
function handleKeyDown(campo, event) {
    const valorAtual = campo.value;

    // Verifica se a tecla pressionada é 'Backspace' e se a barra está no final do valor
    if (event.key === 'Backspace' && valorAtual.endsWith('/')) {
        // Remove a barra do final do valor
        campo.value = valorAtual.substring(0, valorAtual.length - 1);
    }
}


/**
 * Manipula o evento de entrada de dados, formatando dinamicamente o valor do campo de data.
 * Remove caracteres não numéricos, mantendo apenas dígitos. Formata a entrada para o formato "DD/MM/YYYY".
 * 
 * @param {HTMLInputElement} campoData - Elemento input do tipo data no formulário.
 */
function handleInput(campoData) {
    console.log('Evento de formatacao');

    const valorAtual = campoData.value;
    const valorNumerico = valorAtual.replace(/\D/g, '');

    // Verifica o comprimento do valor numérico
    if (valorNumerico.length > 8) {
        // Se for maior que 8, limita para 10 caracteres
        campoData.value = valorAtual.substring(0, 10);
    } else if (valorNumerico.length >= 2 && valorNumerico.length < 2 + 2) {
        // Se estiver entre 2 e 4 caracteres, adiciona a barra após os primeiros 2 dígitos
        campoData.value = valorNumerico.substring(0, 2) + '/' + valorNumerico.substring(2);
    } else if (valorNumerico.length >= 2 + 2 && valorNumerico.length < 8) {
        // Se estiver entre 4 e 8 caracteres, adiciona a barra após os primeiros 2 dígitos e novamente após os próximos 2
        campoData.value = valorNumerico.substring(0, 2) + '/' + valorNumerico.substring(2, 4) + '/' + valorNumerico.substring(4);
    } else {
        // Mantém o valor atual
        campoData.value = valorAtual;
    }
}


function somenteNumerosEBarra(campo) {
    const regexSomenteNumerosEBarra = /[0-9/]+/g;
    campo.value = campo.value.match(regexSomenteNumerosEBarra) || '';
}

