import { atualizaControlePaginacao } from "./pagination.js"
document.addEventListener('DOMContentLoaded', clickEvent);
document.addEventListener('DOMContentLoaded', atualizaControlePaginacao);


// Exporta a função para reatribuir o evento quando ele é removido.
export function clickEventIndexCollection() {
    clickEvent();
}

/**
 * Adiciona um evento de click aos índices das páginas.
 * Captura o elemento pai (.paginacao) e todos os elementos de índice (.index).
 * Adiciona um ouvinte de evento de clique a cada elemento de índice.
 * Executa as funções necessárias quando um índice é clicado.
 * 
 * @private
 */
function clickEvent() {
    const fatherElement = document.querySelector('.paginacao');
    const indexCollection = document.querySelectorAll('.index');

    indexCollection.forEach(element => element.addEventListener('click', (event) => {
        event.preventDefault();
        alteraCurrentPage(element, fatherElement);
        atualizaUrl(element);
        atualizaControlePaginacao();
    }));
}


/**
 * Altera o valor do atributo `currentpage` no elemento <div class="paginacao">.
 * Este atributo precisa estar sempre atualizado para refletir a página atual.
 *
 * @param {HTMLElement} clickedElement - O elemento clicado que contém o atributo de página.
 * @param {HTMLElement} fatherElement - O elemento pai que contém o atributo `currentpage`.
 */
function alteraCurrentPage(clickedElement, fatherElement) {
    const pageElementClicked = clickedElement.dataset.page;
    fatherElement.dataset.currentpage = pageElementClicked;
}

/**
 * Atualiza a URL na barra de navegação do navegador e o histórico de navegação.
 * Captura a página atual do elemento <div class="paginacao"> e atualiza o parâmetro 'page' na URL.
 * Em seguida, usa a API de histórico de navegação para atualizar o histórico de navegação.
 */
function atualizaUrl() {
    let currentPage = document.querySelector('.paginacao').dataset.currentpage;
    const urlAtual = new URL(window.location.href);
    urlAtual.searchParams.set('page', currentPage);
    history.pushState(null, '', urlAtual.href);
}

