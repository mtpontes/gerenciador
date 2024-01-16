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

//se tiver menos que 2 páginas o elemento .paginacao é ocultado
function alteraExibicaoPaginacao(){
	const paginacao = document.querySelector('.paginacao');
	
	const classes = paginacao.classList;
	const totalPages = parseInt(paginacao.dataset.totalpages);
	
	(totalPages < 2) ? classes.add('paginacao-off') : classes.remove('paginacao-off');
}

//atualiza o texto e link dos indices
function atualizaIndices() {
    const currentPage = getCurrentPageFromElementPaginacao();
    const totalPages = getTotalPagesFromElementPaginacao();
    const indexCollection = document.querySelectorAll('.index-page');
    //itera sobre os 3 elementos de index-page
    indexCollection.forEach((element, i) => {
        const indexValue = i + 1;
        //adiciona índices à coleção com base na posição da página atual
        const indexConfig = getIndexConfig(currentPage, totalPages, indexValue);
        //atualiza o texto e link do índice
        element.textContent = indexConfig;
        atualizaDatasetsPageESizes(element, indexConfig);
        //adiciona a classe 'current-index' se for o índice da página atual
        element.classList.toggle('current-index', indexConfig === currentPage);
        //define a visibilidade do terceiro índice se o total de páginas for menor que 3
        element.style.display = (totalPages < 3 && indexValue === 3) ? 'none' : 'inline-block';
    });
}
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
//atualiza o parâmetro 'acao' do acao de todos os elementos .index
function alteraDatasetAcao(){
	const linkCollection = document.querySelectorAll('.index');
	const acao = getParamAcaoFromURL();
	
	linkCollection.forEach(link => {
		link.dataset.acao = acao;
	});
}

//define quando que elementos ellipse são exibidos
function atualizaExibicaoEllipse(){
	const currentPage = getCurrentPageFromElementPaginacao();
	const totalPages = getTotalPagesFromElementPaginacao();
	
	const ellipseStart = document.querySelector('.ellipse.start');
	const ellipseEnd = document.querySelector('.ellipse.end');
	
	(totalPages <= 3) || (currentPage < 3) ? ellipseStart.classList.add('ellipse-off') : ellipseStart.classList.remove('ellipse-off');
	(totalPages <= 3) || (currentPage >= (totalPages -1)) ? ellipseEnd.classList.add('ellipse-off') : ellipseEnd.classList.remove('ellipse-off');
}

//define quando que os botoes para avançar/retornar são exibidos e 
//atualiza seus datasets com base na página atual
function atualizaSetas(){
	const currentPage = getCurrentPageFromElementPaginacao();
	const totalPages = getTotalPagesFromElementPaginacao();
	
	const previousPage = document.querySelector('.pagina.anterior');
	const nextPage = document.querySelector('.pagina.seguinte');

	const updateDatasetSeNecessario = (element, newParamPage) => {
		if(newParamPage >= 1 && newParamPage <= totalPages){
			atualizaDatasetsPageESizes(element, newParamPage);
		}
	}
	//atualiza os datasets
	updateDatasetSeNecessario(previousPage, currentPage -1);
	updateDatasetSeNecessario(nextPage, currentPage + 1);
	//atualiza exibicao diretamente no estilo
	previousPage.style.display = (currentPage == 1) ? 'none' : 'inline-block';
	nextPage.style.display = (currentPage == totalPages) ? 'none' : 'inline-block';
}


//define quando que os índices da primeira e última página são exibidos e demais configs
function atualizaIndexStartEnd() {
	const currentPage = getCurrentPageFromElementPaginacao();
	const totalPages = getTotalPagesFromElementPaginacao();
	
	const startIndex = document.querySelector('.start.index');
	const endIndex = document.querySelector('.end.index');

	//atualiza os datasets
	atualizaDatasetsPageESizes(startIndex, 1);
	atualizaDatasetsPageESizes(endIndex, totalPages);
	
	//atualiza o texto
	startIndex.textContent = '1';
	endIndex.textContent = totalPages;
	
	//atualiza exibicao
	startIndex.style.display = (currentPage < 3) || (totalPages > 1 && totalPages <= 3) ? 'none' : 'inline-block'; 
	endIndex.style.display = (currentPage >= (totalPages -1)) || (totalPages > 1 && totalPages <= 3) ? 'none' : 'inline-block'; 
}
function atualizaDatasetsPageESizes(element, increment){
	const pageSize = getPageSizeFromElementPaginacao();
	
	element.dataset.page = increment;
	element.dataset.size = pageSize;
}




function getParamAcaoFromURL(){
	return new URLSearchParams(window.location.search).get('acao');
}
function getCurrentPageFromElementPaginacao(){
	const currentPage = document.querySelector('.paginacao').dataset.currentpage;
	return isNaN(parseInt(currentPage)) ? 0 : parseInt(currentPage);
}
function getTotalPagesFromElementPaginacao(){
	const totalPages = document.querySelector('.paginacao').dataset.totalpages;
	return isNaN(parseInt(totalPages)) ? 0 : parseInt(totalPages);
}
function getPageSizeFromElementPaginacao(){
	const pageSize = document.querySelector('.paginacao').dataset.pagesize;
	return isNaN(parseInt(pageSize)) ? 0 : parseInt(pageSize);
}