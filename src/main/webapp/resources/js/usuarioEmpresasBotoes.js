//eventos
document.addEventListener("DOMContentLoaded", removeRestauraAJAX);
document.addEventListener("DOMContentLoaded", defineTipoBotao);

//funções
function removeRestauraAJAX() {
	var removeButtons = document.querySelectorAll('.remove-ou-restaura')

	removeButtons.forEach(function (button) {
		button.addEventListener('click', function (event) {
			event.preventDefault();

			var url = this.href;

			var xhr = new XMLHttpRequest();
			xhr.open('POST', url, true);
			xhr.onload = function () {
				if (xhr.status >= 200 && xhr.status < 400) {//se a requisição não falhar
					console.log(xhr.responseText);
					alteraTipoBotao(button);//roda função que altera o botão
				}
			};
			xhr.send();
		})
	})
}
//é chamado no escopo de removeRestauraAJAX
function alteraTipoBotao(button) {
	var ativo = button.dataset.ativo;

	console.log(ativo);
	if (ativo == 'true') {
		button.href = button.href;
		button.classList.remove('remove');
		button.classList.add('restaura');
		button.innerText = 'restaurar';
		button.dataset.ativo = 'false';

	} else if (ativo == 'false') {
		button.href = button.href;
		button.classList.remove('restaura');
		button.classList.add('remove');
		button.innerText = 'remove';
		button.dataset.ativo = 'true';
	}
}

//executa quando o DOM é carregado
//é responsável por diferenteciar o tipo do botão e aplicar as propriedades particulares de cada um
function defineTipoBotao() {
	var buttons = document.querySelectorAll('.remove-ou-restaura');

	buttons.forEach(function (button) {
		var ativo = button.dataset.ativo;

		if (ativo == 'true') {
			button.innerText = 'remove';
			button.classList.add('remove');
		} else {
			button.innerText = 'restaurar';
			button.classList.add('restaura');
		}
	});
	;
}
