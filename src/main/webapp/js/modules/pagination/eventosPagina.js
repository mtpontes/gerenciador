import { atualizaControlePaginacao } from "./pagination.js"
document.addEventListener('DOMContentLoaded', clickEvent);
document.addEventListener('DOMContentLoaded', atualizaControlePaginacao);

// Exporta a função para reatribuir o evento quando ele é removido.
export function clickEventIndexCollection(){
	clickEvent();
}

// Adiciona um evento de click aos indices das páginas
function clickEvent(){
	const fatherElement = document.querySelector('.paginacao');
	const indexCollection = document.querySelectorAll('.index');
	indexCollection.forEach(element => element.addEventListener('click', (event) => {
		event.preventDefault();
		alteraCurrentPage(element, fatherElement);
		atualizaUrl(element);
		atualizaControlePaginacao();
	}));
}

// Altera o valor do atributo `currentpage` no elemento <div class="paginacao">
// Asse atributo precisa estar sempre atualizado
function alteraCurrentPage(clickedElement, fatherElement) {
    const pageElementClicked = clickedElement.dataset.page;
    fatherElement.dataset.currentpage = pageElementClicked;
}

// Atualiza a URL na barra de navegação do browser e o histórico de navegação
function atualizaUrl(){
	let currentPage = document.querySelector('.paginacao').dataset.currentpage;
	const urlAtual = new URL(window.location.href);
	urlAtual.searchParams.set('page', currentPage);
	history.pushState(null, '', urlAtual.href);
}
