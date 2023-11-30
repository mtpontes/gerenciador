import { getRequest } from "../ajax/ajax.js";
import { usuarioRegexPatterns } from "./regexPatterns.js";

document.addEventListener("DOMContentLoaded", validaComRequest);

async function validaComRequest(){
//	const url = "/gerenciador/ajax?acao=verificaLogin";
	const url = "http://localhost:8080/gerenciador/ajax?acao=verificaLogin";
	let respostaRequisicaoAJAX;
	
    const formulario = document.getElementById('formulario');
    const campos = [
	    { input: document.getElementById('nome'), regex: usuarioRegexPatterns.NOME_USUARIO_PATTERN, erro: document.getElementById('nomeErro'), nomeDoCampo: 'nome' },
	    { input: document.getElementById('login'), regex: usuarioRegexPatterns.LOGIN_USUARIO_PATTERN, erro: document.getElementById('loginErro'), nomeDoCampo: 'login' } ,
	    { input: document.getElementById('senha'), regex: usuarioRegexPatterns.SENHA_USUARIO_PATTERN, erro: document.getElementById('senhaErro'), nomeDoCampo: 'senha' },
	    { input: document.getElementById('confirma'), regex: usuarioRegexPatterns.CONFIRMA_SENHA_USUARIO_PATTERN, erro: document.getElementById('confirmaErro'), nomeDoCampo: 'confirma' }
	];
    ocultaMensagemDeErroAoDigitar(campos);
    
	const houveAlgumErro = validaCamposComRequest(campos, url, respostaRequisicaoAJAX, formulario);
	submeteFormulario(formulario, houveAlgumErro);
}


function ocultaMensagemDeErroAoDigitar(campos){
    campos.forEach(function(campo) {
    	campo.input.addEventListener('input', function() {
        	campo.erro.style.display = 'none';
    	});
	});
}

async function validaCamposComRequest(campos, url, respostaRequisicaoAJAX, formulario){
    formulario.addEventListener('submit', async function(event){
		event.preventDefault();
	
		//captura o texto digitado no campo login para consultar no banco se ele já existe
		let valorDoCampo = campos[1].input.value.trim();
		
		const parametroUrl = {
			acao: 'verificaLogin',
			login: valorDoCampo
		}
		
		respostaRequisicaoAJAX = await getRequest(url, parametroUrl);
		validaCampos(campos, respostaRequisicaoAJAX.response, formulario);
	});
}

async function validaCampos(campos, respostaRequisicao, formulario) {
    let houveErro = false;

	const validacoesParticulares = {
		'login':(campo) => {
			validaLogin(campo, respostaRequisicao);
			validaCamposComRegex(campo);
		},
		'confirma': (campo) => {
			validaConfirma(campo, campos[2].input.value);
			validaCamposComRegex(campo);
		}
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
	}
	console.log('Submetendo formulario...'), 
	formulario.submit();
}