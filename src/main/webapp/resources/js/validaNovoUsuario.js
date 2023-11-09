import { postRequest } from "./ajax.js";
document.addEventListener("DOMContentLoaded", validaComRequest);

async function validaComRequest(){
	const url = "/gerenciador/ajax?acao=verificaLogin";
	let respostaRequisicaoAJAX;
	
    const formulario = document.getElementById('formulario');
    const campos = [
	    { input: document.getElementById('nome'), regex: /^[A-Za-zÀ-ÖØ-öø-ÿ' _-]+$/, erro: document.getElementById('nomeErro'), nomeDoCampo: 'nome' },
	    { input: document.getElementById('login'), regex: /^[a-zA-Z0-9_.]{3,20}$/, erro: document.getElementById('loginErro'), nomeDoCampo: 'login' } ,
	    { input: document.getElementById('senha'), regex: /.{8,}/, erro: document.getElementById('senhaErro'), nomeDoCampo: 'senha' },
	    { input: document.getElementById('confirma'), regex: /.+/, erro: document.getElementById('confirmaErro'), nomeDoCampo: 'confirma' }
	];
	
	//após uma tentativa fracassada de submeter o formulário, sempre que o usuário modificar um campo a mensagem de erro irá desaparecer
    campos.forEach(function(campo) {
        campo.input.addEventListener('input', function() {
            campo.erro.style.display = 'none';
        });
    });
	
	//sempre que o formulário for submetido os campos preenchidos serão validados
    formulario.addEventListener('submit', async function(event){
		event.preventDefault();
		//captura o texto digitado no campo login para consultar no banco se ele já existe
		let valorDoCampo = campos[1].input.value.trim();
		
		//cria o corpo da requisição
		const corpo = {
			acao: 'verificaLogin',
			valorDeLogin: valorDoCampo
		}
		//faz a requisição AJAX		
		respostaRequisicaoAJAX = await postRequest(url, corpo);
		//valida os campos
		validaCampos(campos, respostaRequisicaoAJAX, formulario);
	});
}

function validaCampos(campos, respostaRequisicao, formulario) {

	let naoHouveErro = true;

	campos.forEach(function(campo) {
		const valor = campo.input.value.trim();

		//se não estiver de acordo com o regex
		//ou se o campo atual for igual o campo 'Confirma' E o texto digitado for diferente do texto digitado no campo 'Senha'
		//ou se a requisição AJAX confirmou que esse login já existe no banco E o campo atual é o campo 'Login'
		if (!campo.regex.test(valor) || (campo.nomeDoCampo === 'confirma' && valor !== campos[2].input.value) || (respostaRequisicao.resposta == true && campo.nomeDoCampo == 'login') ) {
			console.log(campo.nomeDoCampo.toUpperCase(), ': ERROR')
			
			//Define a mensagem de erro dinâmicamente de acordo com a resposta da requisição AJAX
			defineErroCampoLogin(campos[1], respostaRequisicao.resposta)
			campo.erro.style.display = 'block';
			naoHouveErro = false;
		} else {
			console.log(campo.nomeDoCampo.toUpperCase(), ': SUCCES')
		}
	});
	
	//se naoHouveErro se manter true, submete o formulário para o servidor
	naoHouveErro ? (console.log('Submetendo formulario...'), formulario.submit()) : console.log('O formulário não passou na validação!');
}


function defineErroCampoLogin(campoLogin, respostaRequisicao) {
	//se o login já existe no banco, a mensagem de erro será sobre o login já existir
	respostaRequisicao ? (
		campoLogin.erro.style.color = 'yellow',
		campoLogin.erro.innerText = 'O login informado já está em uso. Por favor, escolha outro login'
	) : (
		//se o login não existe no banco, a mensagem de erro será sobre a regra de formatação
		campoLogin.erro.style.color = 'red',
		campoLogin.erro.innerText = 'Login precisa ter no mínimo 3 caracteres'
	);
}