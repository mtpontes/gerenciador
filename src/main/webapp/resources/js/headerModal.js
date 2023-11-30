document.addEventListener("DOMContentLoaded", userModal);

function userModal() {
	console.log('executando mostrarModal()');
	
	let iconeUsuario = document.getElementById('icone-usuario');
	let modal = document.getElementById('modal');

	iconeUsuario.addEventListener('mouseenter', () =>{modal.style.display = 'block'});
	modal.addEventListener('mouseenter', () =>{modal.style.display = 'block'});
	modal.addEventListener('mouseleave', () =>{modal.style.display = 'none'});
}

