document.addEventListener("DOMContentLoaded", userModal);

function userModal() {
	const iconeUsuario = document.getElementById('icone-usuario');
	const modal = document.getElementById('modal');
	
	function mostraModal(){
		modal.style.display = 'block';
	}
	
	function escondeModal(){
		modal.style.display = 'none';
	}

	//quando o mouse passa por cima exibe o modal
	iconeUsuario.addEventListener('mouseenter', mostraModal);
	//enquanto o mouse estiver na área do modal mantém exibido
	modal.addEventListener('mouseenter', mostraModal);
	//fecha o modal quando o mouse sai da área
	modal.addEventListener('mouseleave', escondeModal);
}

