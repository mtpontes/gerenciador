//eventos
document.addEventListener("DOMContentLoaded", userModal);

//funções
function userModal() {
	console.log('executando mostrarModal()');
	
	let iconeUsuario = document.getElementById('icone-usuario');

	iconeUsuario.addEventListener('mouseenter', () =>{modal.style.display = 'block'});
	modal.addEventListener('mouseenter', () =>{modal.style.display = 'block'});
	modal.addEventListener('mouseleave', () =>{modal.style.display = 'none'});
}

