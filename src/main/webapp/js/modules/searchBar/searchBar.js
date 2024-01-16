document.addEventListener('DOMContentLoaded', eventSubmitAtualizaParamNomeEmpresaUrl);
document.addEventListener('DOMContentLoaded', preencheCampoInputComUltimaPesquisa);
document.addEventListener("DOMContentLoaded", exibeBotaoLimparBarra);

function eventSubmitAtualizaParamNomeEmpresaUrl(){
	const searchInputElement = document.querySelector('.form-search');
	searchInputElement.addEventListener('submit', () => {
		atualizaUrlComUltimaPesquisa();
	});
}

function preencheCampoInputComUltimaPesquisa(){
	const textoPesquisado = new URL(window.location.href).searchParams.get('nomeEmpresa');
	const campoInput = document.getElementById('search-input');

	campoInput.value = textoPesquisado;
}

function atualizaUrlComUltimaPesquisa(){
	const textoPesquisado = document.querySelector('.search-input').value;
	
	const url = new URL(window.location.href);
	url.searchParams.set('nomeEmpresa', textoPesquisado);
	
	history.pushState(null, '', url.href);
}


//BOTAO LIMPAR SEARCH BAR
function exibeBotaoLimparBarra(){
	const searchInput = document.getElementById('search-input');
	const botaoLimpar = document.getElementById('botao-limpar');
	
	//se o input estiver populado, o botao ficará visível assim que a página carregar
	exibicaoBotaoLimparBarra(searchInput, botaoLimpar);
	
	//o botao sera exibido se houver algum input
	searchInput.addEventListener('input', () => {
		exibicaoBotaoLimparBarra(searchInput, botaoLimpar);
	});
	botaoLimpar.addEventListener('click', () => {
		apagaTextoDoCampo(searchInput, botaoLimpar);
	});
}

function exibicaoBotaoLimparBarra(searchInput, botaoLimpar){
	botaoLimpar = document.getElementById('botao-limpar');
	const valorDoCampoInput = searchInput.value;
	//verifica se valorDoCampoInput é uma String não vazia, se sim, recebe 'inline-block'
	botaoLimpar.style.display = valorDoCampoInput ? 'inline-block' : 'none';
}
function apagaTextoDoCampo(searchInput, botaoLimpar){
	//apaga todo conteúdo no campo input
	searchInput.value = '';
	//oculta o botão de limpar
	botaoLimpar.style.display = 'none';
	//mantém o campo input em foco
	searchInput.focus();
}

