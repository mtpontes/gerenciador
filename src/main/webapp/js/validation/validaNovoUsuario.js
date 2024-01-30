import { postRequest } from "../util/ajaxUtil.js";
import { usuarioRegexPatterns } from './regex/usuarioRegexPatterns.js';
document.addEventListener("DOMContentLoaded", validaComRequest);

/**
 * Função que realiza a validação de campos com uma requisição AJAX para verificar o login.
 * Utiliza uma URL específica para a verificação e manipula mensagens de erro durante a digitação.
 */
function validaComRequest() {
    const url = window.location.origin + "/gerenciador/usuario?acao=verificaLogin";
    
    const formulario = document.getElementById('formulario');
    const campos = [
        { input: document.getElementById('nome'), regex: usuarioRegexPatterns.NOME_USUARIO_PATTERN, erro: document.getElementById('nomeErro'), nomeDoCampo: 'nome' },
        { input: document.getElementById('login'), regex: usuarioRegexPatterns.LOGIN_USUARIO_PATTERN, erro: document.getElementById('loginErro'), nomeDoCampo: 'login' },
        { input: document.getElementById('senha'), regex: usuarioRegexPatterns.SENHA_USUARIO_PATTERN, erro: document.getElementById('senhaErro'), nomeDoCampo: 'senha' },
        { input: document.getElementById('confirma'), regex: usuarioRegexPatterns.CONFIRMA_SENHA_USUARIO_PATTERN, erro: document.getElementById('confirmaErro'), nomeDoCampo: 'confirma' }
    ];

    ocultaMensagemDeErroAoDigitar(campos);
    validaCamposComRequest(campos, url, formulario);
}



/**
 * Oculta as mensagens de erro associadas a cada campo ao usuário começar a digitar.
 * Adiciona um evento 'input' a cada campo, que esconde a mensagem de erro ao detectar entrada do usuário.
 * 
 * @param {Array} campos - Um array contendo objetos que representam os campos a serem validados.
 */
function ocultaMensagemDeErroAoDigitar(campos) {
    campos.forEach(function(campo) {
        campo.input.addEventListener('input', function() {
            campo.erro.style.display = 'none';
        });
    });
}


/**
 * Realiza uma validação assíncrona dos campos de um formulário utilizando uma requisição AJAX.
 * Captura o texto digitado no campo de login para consultar no banco se ele já existe.
 * Exibe ou oculta mensagens de erro associadas a cada campo com base na resposta da requisição.
 * Submete o formulário se não houver erros.
 * 
 * @param {Array} campos - Um array contendo objetos que representam os campos a serem validados.
 * @param {string} url - A URL para a qual a requisição AJAX será feita.
 * @param {HTMLElement} formulario - O elemento HTML do formulário.
 */
function validaCamposComRequest(campos, url, formulario) {
    formulario.addEventListener('submit', async function(event) {
        event.preventDefault();

        // Captura o texto digitado no campo login para consultar no banco se ele já existe
        let valorDoCampo = campos[1].input.value.trim();

        const params = {
            login: valorDoCampo
        };

        // Faz uma requisição AJAX utilizando a função postRequest e obtém o resultado
        const resultado = await postRequest(url, params);
        console.log(resultado);
        const respostaRequisicaoAJAX = resultado.response;

        // Realiza a validação dos campos com base na resposta da requisição
        const houveAlgumErro = validaCampos(campos, respostaRequisicaoAJAX);

        // Submete o formulário se não houver erros
        submeteFormulario(formulario, houveAlgumErro);
    });
}

/**
 * Realiza a validação dos campos de um formulário com base em uma resposta de requisição AJAX.
 * 
 * @param {Array} campos - Um array contendo objetos que representam os campos a serem validados.
 * @param {Object} respostaRequisicao - A resposta da requisição AJAX, contendo informações sobre a validação.
 * @returns {boolean} - Retorna true se houver algum erro durante a validação, caso contrário, retorna false.
 */
function validaCampos(campos, respostaRequisicao) {
    let houveErro = false;
    let valorCampoSenha = campos[2].input.value;

    const validacoesParticulares = {
        login: (campo) => validaLogin(campo, respostaRequisicao) || validaCamposComRegex(campo),
        confirma: (campo) => validaConfirma(campo, valorCampoSenha) || validaCamposComRegex(campo)
    };

    campos.forEach(campo => {
        // Executa validações particulares se existirem para o campo, caso contrário, executa a validação padrão
        houveErro = validacoesParticulares[campo.nomeDoCampo] ? validacoesParticulares[campo.nomeDoCampo](campo) : houveErro;
        houveErro = validaCamposComRegex(campo) || houveErro;
    });

    return houveErro;
}


/**
 * Valida o campo de login com base na resposta da requisição AJAX.
 * 
 * @param {Object} campo - Objeto que representa o campo a ser validado.
 * @param {Object} respostaAJAX - Resposta da requisição AJAX, contendo informações sobre a validação do login.
 * @returns {boolean} - Retorna true se houver erro (login já em uso), caso contrário, retorna false.
 */
function validaLogin(campo, respostaAJAX) {
    if (respostaAJAX) {
        console.log(campo.nomeDoCampo.toUpperCase(), ': ERROR - Login já está em uso');
        campo.erro.style.display = 'block';
        defineErroCampoLogin(campo, respostaAJAX);
        return true;
    }
    // Se o login não está em uso, a mensagem de erro precisa ser alterada
    defineErroCampoLogin(campo, respostaAJAX);
    return false;
}

/**
 * Valida o campo de confirmação comparando-o com o texto no campo de senha.
 * 
 * @param {Object} campo - Objeto que representa o campo de confirmação a ser validado.
 * @param {string} textoNoCampoSenha - Texto contido no campo de senha a ser comparado.
 * @returns {boolean} - Retorna true se houver erro (campo de confirmação não é igual ao campo de senha), caso contrário, retorna false.
 */
function validaConfirma(campo, textoNoCampoSenha) {
    const textoNoCampoConfirma = campo.input.value;

    if (textoNoCampoConfirma !== textoNoCampoSenha) {
        console.log(campo.nomeDoCampo.toUpperCase(), ': ERROR - Não é igual a senha');
        campo.erro.style.display = 'block';
        return true;
    }
    return false;
}


/**
 * Valida um campo específico utilizando uma expressão regular.
 * 
 * @param {Object} campo - Objeto que representa o campo a ser validado.
 * @returns {boolean} - Retorna true se houver erro (campo não atende à expressão regular), caso contrário, retorna false.
 */
function validaCamposComRegex(campo) {
    const valorCampo = campo.input.value.trim();
    const regex = campo.regex;

    const re = new RegExp(regex);
    if (!re.test(valorCampo)) {
        console.log(campo.nomeDoCampo.toUpperCase(), ': ERROR');
        campo.erro.style.display = 'block';
        return true;
    }
    return false;
}


/**
 * Define a mensagem de erro para o campo de login com base na resposta da requisição AJAX.
 * 
 * @param {Object} campo - Objeto que representa o campo de login.
 * @param {boolean} respostaRequisicao - Indica se houve uma resposta positiva na requisição AJAX (login já existente).
 */
function defineErroCampoLogin(campo, respostaRequisicao) {
    if (respostaRequisicao) {
        campo.erro.style.color = 'yellow';
        campo.erro.innerText = 'O login informado já está em uso. Por favor, escolha outro login';
    } else {
        campo.erro.style.color = 'red';
        campo.erro.innerText = 'Login precisa ter no mínimo 3 caracteres';
    }
}

/**
 * Submete o formulário se não houver erros na validação.
 * 
 * @param {HTMLFormElement} formulario - Elemento do formulário a ser submetido.
 * @param {boolean} houveErro - Indica se houve algum erro durante a validação dos campos.
 */
function submeteFormulario(formulario, houveErro) {
    if (houveErro) {
        console.log('O formulário não passou na validação!');
    } else {
        console.log('Submetendo formulário...');
        formulario.submit();
    }
}
