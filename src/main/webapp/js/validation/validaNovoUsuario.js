import { postRequest } from "../util/ajax.js";
import { usuarioRegexPatterns } from '../util/regexPatterns.js';

document.addEventListener("DOMContentLoaded", validaComRequest);

function validaComRequest(){
	const url = window.location.origin + "/gerenciador/usuario?acao=verificaLogin";
	
    const formulario = document.getElementById('formulario');
    const campos = [
	    { input: document.getElementById('nome'), regex: usuarioRegexPatterns.NOME_USUARIO_PATTERN, erro: document.getElementById('nomeErro'), nomeDoCampo: 'nome' },
	    { input: document.getElementById('login'), regex: usuarioRegexPatterns.LOGIN_USUARIO_PATTERN, erro: document.getElementById('loginErro'), nomeDoCampo: 'login' } ,
	    { input: document.getElementById('senha'), regex: usuarioRegexPatterns.SENHA_USUARIO_PATTERN, erro: document.getElementById('senhaErro'), nomeDoCampo: 'senha' },
	    { input: document.getElementById('confirma'), regex: usuarioRegexPatterns.CONFIRMA_SENHA_USUARIO_PATTERN, erro: document.getElementById('confirmaErro'), nomeDoCampo: 'confirma' }
	];
    ocultaMensagemDeErroAoDigitar(campos);
	validaCamposComRequest(campos, url, formulario);
}


function ocultaMensagemDeErroAoDigitar(campos){
    campos.forEach(function(campo) {
    	campo.input.addEventListener('input', function() {
        	campo.erro.style.display = 'none';
    	});
	});
}

function validaCamposComRequest(campos, url, formulario){
    formulario.addEventListener('submit', async function(event){
		event.preventDefault();
	
		//captura o texto digitado no campo login para consultar no banco se ele já existe
		let valorDoCampo = campos[1].input.value.trim();
		
		const params = {
			login: valorDoCampo
		}
		
		const resultado = await postRequest(url, params);
		console.log(resultado);
		const respostaRequisicaoAJAX = resultado.response;
		const houveAlgumErro = validaCampos(campos, respostaRequisicaoAJAX);
		submeteFormulario(formulario, houveAlgumErro);
	});
}

function validaCampos(campos, respostaRequisicao) {
    let houveErro = false;
    let valorCampoSenha = campos[2].input.value;

	const validacoesParticulares = {
		login:(campo) =>  validaLogin(campo, respostaRequisicao) || validaCamposComRegex(campo),
		confirma: (campo) =>  validaConfirma(campo, valorCampoSenha) || validaCamposComRegex(campo)
	}
	
    campos.forEach(campo => {
        houveErro = validacoesParticulares[campo.nomeDoCampo] ? validacoesParticulares[campo.nomeDoCampo](campo) : houveErro;
		houveErro = validaCamposComRegex(campo) || houveErro;
    });
	
	return houveErro;
}

function validaLogin(campo, respostaAJAX){
	if(respostaAJAX){
		console.log(campo.nomeDoCampo.toUpperCase(), ': ERROR - Login já está em uso')
		campo.erro.style.display = 'block';
		defineErroCampoLogin(campo, respostaAJAX);
		return true;
	}
	//se o login já existe, a mensagem de erro precisa ser alterada
	defineErroCampoLogin(campo, respostaAJAX);
	return false;
}

function validaConfirma(campo, textoNoCampoSenha){
	const textoNoCampoConfirma = campo.input.value;
	
	if(textoNoCampoConfirma !== textoNoCampoSenha){
		console.log(campo.nomeDoCampo.toUpperCase(), ': ERROR - Não é igual a senha')
		campo.erro.style.display = 'block';
		return true;
	}
	return false
}

function validaCamposComRegex(campo){
	const valorCampo = campo.input.value.trim()
	const regex = campo.regex;
	
	const re = new RegExp(regex);
	if(!re.test(valorCampo)){
		console.log(campo.nomeDoCampo.toUpperCase(), ': ERROR')
		campo.erro.style.display = 'block';
		return true;
	}
	return false;
}

function defineErroCampoLogin(campo, respostaRequisicao) {
	//se o login já existe no banco, a mensagem de erro será sobre o login já existir
	if(respostaRequisicao){
		campo.erro.style.color = 'yellow',
		campo.erro.innerText = 'O login informado já está em uso. Por favor, escolha outro login'
		
	} else {
		//mensagem de erro padrão
		campo.erro.style.color = 'red',
		campo.erro.innerText = 'Login precisa ter no mínimo 3 caracteres'
	}
}

function submeteFormulario(formulario, houveErro){
	if(houveErro){
		console.log('O formulário não passou na validação!')
		return;
		
	} else {
		console.log('Submetendo formulario...');
		formulario.submit();
	}
}