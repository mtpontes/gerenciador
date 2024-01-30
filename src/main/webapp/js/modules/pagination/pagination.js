export function atualizaControlePaginacao(){
	alteraExibicaoPaginacao();
	atualizaIndices();
	alteraDatasetAcao();
	atualizaExibicaoEllipse();
	atualizaIndexStartEnd();
	atualizaSetas();
}
export function logicaPaginacao(){
	atualizaIndices();
	alteraDatasetAcao();
	atualizaExibicaoEllipse();
	atualizaIndexStartEnd();
	atualizaSetas();
	alteraExibicaoPaginacao();
}

function atualizaDatasetsPageSize(element, increment){
	const pageSize = getPageSizeFromElementPaginacao();
	
	element.dataset.page = increment;
	element.dataset.size = pageSize;
}

/**
 * Altera a exibição do elemento .paginacao com base no número total de páginas.
 * Se houver menos de 2 páginas, o elemento .paginacao é ocultado; caso contrário, é exibido.
 */
function alteraExibicaoPaginacao() {
    const paginacao = document.querySelector('.paginacao');
    const classes = paginacao.classList;
    const totalPages = parseInt(paginacao.dataset.totalpages);

    if (totalPages < 2) {
        classes.add('paginacao-off'); // Oculta o elemento .paginacao
    } else {
        classes.remove('paginacao-off'); // Exibe o elemento .paginacao
    }
}

/**
 * Atualiza o texto e link dos índices de página.
 * Obtém informações sobre a página atual e o número total de páginas.
 * Itera sobre os elementos .index-page e atualiza seus textos, links e visibilidade.
 */
function atualizaIndices() {
    // Obtém informações sobre a página atual e o número total de páginas
    const currentPage = getCurrentPageFromElementPaginacao();
    const totalPages = getTotalPagesFromElementPaginacao();
    const indexCollection = document.querySelectorAll('.index-page');

    // Itera sobre os elementos de .index-page
    indexCollection.forEach((element, i) => {
        const indexValue = i + 1;

        // Obtém informações sobre a configuração do índice com base na posição da página atual
        const indexConfig = getIndexConfig(currentPage, totalPages, indexValue);

        // Atualiza o texto e o link do índice
        element.textContent = indexConfig;
        atualizaDatasetsPageSize(element, indexConfig);

        // Adiciona a classe 'current-index' se for o índice da página atual
        element.classList.toggle('current-index', indexConfig === currentPage);

        // Define a visibilidade do terceiro índice se o total de páginas for menor que 3
        element.style.display = (totalPages < 3 && indexValue === 3) ? 'none' : 'inline-block';
    });
}
/**
 * Obtém a configuração do índice com base na posição da página atual, número total de páginas e valor do índice.
 * Se o total de páginas for menor ou igual a 3, retorna o próprio valor do índice.
 * Caso contrário, ajusta o valor do índice com base na posição da página atual.
 */
function getIndexConfig(currentPage, totalPages, indexValue) {
    if (totalPages <= 3) {
        return indexValue;
    } else {
        if (currentPage === 1) {
            return indexValue;
        } else if (currentPage === totalPages) {
            return totalPages - 3 + indexValue;
        } else {
            return currentPage - 2 + indexValue;
        }
    }
}


/**
 * Atualiza o parâmetro 'acao' do dataset de todos os elementos .index.
 * Obtém o valor do parâmetro 'acao' da URL e atribui a todos os elementos .index.
 */
function alteraDatasetAcao(){
    const linkCollection = document.querySelectorAll('.index');
    const acao = getParamAcaoFromURL();
    
    linkCollection.forEach(link => {
        link.dataset.acao = acao;
    });
}

/**
 * Define a exibição dos elementos ellipse com base na posição da página atual e no número total de páginas.
 * Se o total de páginas for menor ou igual a 3, ou a página atual for menor que 3, oculta o elemento ellipse de início.
 * Se o total de páginas for menor ou igual a 3, ou a página atual for maior ou igual a (totalPages - 1), oculta o elemento ellipse de fim.
 */
function atualizaExibicaoEllipse(){
    const currentPage = getCurrentPageFromElementPaginacao();
    const totalPages = getTotalPagesFromElementPaginacao();
    
    const ellipseStart = document.querySelector('.ellipse.start');
    const ellipseEnd = document.querySelector('.ellipse.end');
    
    (totalPages <= 3) || (currentPage < 3) ? ellipseStart.classList.add('ellipse-off') : ellipseStart.classList.remove('ellipse-off');
    (totalPages <= 3) || (currentPage >= (totalPages -1)) ? ellipseEnd.classList.add('ellipse-off') : ellipseEnd.classList.remove('ellipse-off');
}
/**
 * Define quando os botões para avançar/retornar são exibidos e
 * atualiza seus datasets com base na página atual.
 */
function atualizaSetas(){
    const currentPage = getCurrentPageFromElementPaginacao();
    const totalPages = getTotalPagesFromElementPaginacao();
    
    const previousPage = document.querySelector('.pagina.anterior');
    const nextPage = document.querySelector('.pagina.seguinte');

    // Função auxiliar para atualizar o dataset do elemento se necessário
    const updateDatasetSeNecessario = (element, newParamPage) => {
        if(newParamPage >= 1 && newParamPage <= totalPages){
            atualizaDatasetsPageSize(element, newParamPage);
        }
    }

    // Atualiza os datasets
    updateDatasetSeNecessario(previousPage, currentPage - 1);
    updateDatasetSeNecessario(nextPage, currentPage + 1);

    // Atualiza a exibição diretamente no estilo
    previousPage.style.display = (currentPage == 1) ? 'none' : 'inline-block';
    nextPage.style.display = (currentPage == totalPages) ? 'none' : 'inline-block';
}
/**
 * Define a exibição dos índices da primeira e última página e atualiza outras configurações.
 */
function atualizaIndexStartEnd() {
    const currentPage = getCurrentPageFromElementPaginacao();
    const totalPages = getTotalPagesFromElementPaginacao();
    
    const startIndex = document.querySelector('.start.index');
    const endIndex = document.querySelector('.end.index');

    // Atualiza os datasets
    atualizaDatasetsPageSize(startIndex, 1);
    atualizaDatasetsPageSize(endIndex, totalPages);
    
    // Atualiza o texto
    startIndex.textContent = '1';
    endIndex.textContent = totalPages;
    
    // Atualiza a exibição
    startIndex.style.display = (currentPage < 3) || (totalPages > 1 && totalPages <= 3) ? 'none' : 'inline-block'; 
    endIndex.style.display = (currentPage >= (totalPages -1)) || (totalPages > 1 && totalPages <= 3) ? 'none' : 'inline-block'; 
}



// Captura o valor do parametro 'acao' da URL
function getParamAcaoFromURL(){
	return new URLSearchParams(window.location.search).get('acao');
}
// Captura a o valor do dataset `currentPage` do elemento `.paginacao`
function getCurrentPageFromElementPaginacao(){
	const currentPage = document.querySelector('.paginacao').dataset.currentpage;
	return isNaN(parseInt(currentPage)) ? 0 : parseInt(currentPage);
}
// Captura a o valor do dataset `totalPages` do elemento `.paginacao`
function getTotalPagesFromElementPaginacao(){
	const totalPages = document.querySelector('.paginacao').dataset.totalpages;
	return isNaN(parseInt(totalPages)) ? 0 : parseInt(totalPages);
}
// Captura a o valor do dataset `pageSize` do elemento `.paginacao`
function getPageSizeFromElementPaginacao(){
	const pageSize = document.querySelector('.paginacao').dataset.pagesize;
	return isNaN(parseInt(pageSize)) ? 0 : parseInt(pageSize);
}