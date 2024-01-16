//altera as propriedades dataset de .pagination
export function alteraDadosPaginacao(pagination){
	const paginacao = document.querySelector('.paginacao');
	paginacao.dataset.currentpage = pagination.pageNumber;
	paginacao.dataset.totalpages = pagination.totalPages;
	paginacao.dataset.pagesize = pagination.pageSize;
}
//atualiza o parâmetro 'acao' da URL
export function atualizaParamAcaoUrl(acao){
	const urlAtual = new URL(window.location.href);
	urlAtual.searchParams.set('acao', acao);
	history.pushState(null, '', urlAtual.href);
}