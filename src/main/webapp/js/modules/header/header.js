document.addEventListener("DOMContentLoaded", userModal);

//MODAL
function userModal() {
	let iconeUsuario = document.getElementById('icone-usuario');
	let modal = document.getElementById('modal');

	iconeUsuario.addEventListener('mouseenter', () =>{modal.style.display = 'block'});
	modal.addEventListener('mouseenter', () =>{modal.style.display = 'block'});
	modal.addEventListener('mouseleave', () =>{modal.style.display = 'none'});
}