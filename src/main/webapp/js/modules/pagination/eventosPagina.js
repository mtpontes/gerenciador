import { atualizaControlePaginacao } from "./pagination.js"
document.addEventListener('DOMContentLoaded', clickEvent);
document.addEventListener('DOMContentLoaded', atualizaControlePaginacao);

export function clickEventIndexCollection(){
	clickEvent();
}

//adiciona um evento de click aos indices das páginas
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

//altera o valor do atributo currentpage no elemento <div class="paginacao">
//esse atributo é lido por várias outras funções, por isso precisa estar sempre atualizado
function alteraCurrentPage(clickedElement, fatherElement) {
    const pageElementClicked = clickedElement.dataset.page;
    fatherElement.dataset.currentpage = pageElementClicked;
}

//atualiza a URL na barra de navegação do browser e o histórico de navegação
function atualizaUrl(){
	let currentPage = document.querySelector('.paginacao').dataset.currentpage;
	const urlAtual = new URL(window.location.href);
	urlAtual.searchParams.set('page', currentPage);
	history.pushState(null, '', urlAtual.href);
}
