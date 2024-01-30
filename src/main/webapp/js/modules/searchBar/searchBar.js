document.addEventListener('DOMContentLoaded', eventSubmitAtualizaParamNomeEmpresaUrl);
document.addEventListener('DOMContentLoaded', preencheCampoInputComUltimaPesquisa);
document.addEventListener("DOMContentLoaded", exibeBotaoLimparBarra);

/**
 * Adiciona um evento de submit ao formulário de pesquisa para atualizar a URL com o último termo pesquisado.
 */
function eventSubmitAtualizaParamNomeEmpresaUrl(){
    const searchInputElement = document.querySelector('.form-search');
    searchInputElement.addEventListener('submit', () => {
        atualizaUrlComUltimaPesquisa();
    });
}

/**
 * Preenche o campo de input com o último termo pesquisado obtido da URL.
 */
function preencheCampoInputComUltimaPesquisa(){
    const textoPesquisado = new URL(window.location.href).searchParams.get('nomeEmpresa');
    const campoInput = document.getElementById('search-input');

    campoInput.value = textoPesquisado;
}

/**
 * Atualiza a URL na barra de navegação do browser e o histórico de navegação com o último termo pesquisado.
 */
function atualizaUrlComUltimaPesquisa(){
    const textoPesquisado = document.querySelector('.search-input').value;

    const url = new URL(window.location.href);
    url.searchParams.set('nomeEmpresa', textoPesquisado);

    history.pushState(null, '', url.href);
}



/**
 * Exibe o botão de limpar a barra de pesquisa se o campo de input estiver populado.
 * 
 * @param {HTMLElement} searchInput - Elemento HTML representando o campo de input de pesquisa.
 * @param {HTMLElement} botaoLimpar - Elemento HTML representando o botão de limpar a barra de pesquisa.
 */
function exibeBotaoLimparBarra(){
    const searchInput = document.getElementById('search-input');
    const botaoLimpar = document.getElementById('botao-limpar');

    // Se o input estiver populado, o botão ficará visível assim que a página carregar
    exibicaoBotaoLimparBarra(searchInput, botaoLimpar);

    // O botão será exibido se houver algum input
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
	// Verifica se valorDoCampoInput é uma String não vazia, se sim, recebe 'inline-block'
	botaoLimpar.style.display = valorDoCampoInput ? 'inline-block' : 'none';
}
function apagaTextoDoCampo(searchInput, botaoLimpar){
	// Apaga todo conteúdo no campo input
	searchInput.value = '';
	// Oculta o botão de limpar
	botaoLimpar.style.display = 'none';
	// Mantém o campo input em foco
	searchInput.focus();
}

