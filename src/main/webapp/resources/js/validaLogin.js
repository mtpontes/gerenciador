document.addEventListener("DOMContentLoaded", valida);

function valida(){
	const formulario = document.getElementById('formulario');
		formulario.addEventListener('submit', function(event){
			
		const login = document.getElementById('login');
		const loginRegex = /^[a-zA-Z0-9_.]{3,20}$/;
		let mensagemLogin = 'O login deve conter apenas letras, números, underscors(_) e (pontos) e ter entre 3 a 20 caracteres.';
			
		verifica(login, event, loginRegex, mensagemLogin);
		
		const senha = document.getElementById('senha');
		const senhaRegex = /.{8,}/; 
		let mensagemSenha = 'A senha precisa ter no mínimo 8 caracteres.';
			
		verifica(senha, event, senhaRegex, mensagemSenha);
		});
}

function verifica(elemento, evento, regra, mensagem){
	const valorDoEvento = elemento.value.trim();
	console.log("o valor do evento é "  + valorDoEvento);
	
	if(!regra.test(valorDoEvento)) {
		evento.preventDefault();
		console.log("Não passou na validação!!");
		login.setCustomValidity(mensagem);
	}
}