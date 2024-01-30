import { API_CONFIG } from "../../util/api-config.js";
import { getRequest } from "../../util/ajax.js";
import { EventManager } from "../../util/eventManagerUtil.js";

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


/**
 * Adiciona eventos de clique para elementos de paginação.
 * @param {Object} params - Parâmetros opcionais para a requisição.
 * @param {Object} adicionador - Objeto para adicionar eventos adicionais.
 * @param {Object} elementFactory - Objeto que define a reconstrução dos elementos removidos.
 */
export const clickEventPaginationtIndex = (params = null, adicionador = null, elementFactory) => {
	const clickHandler = async (event) => {
	    event.preventDefault();
	    const urlRelativa = API_CONFIG.EMPRESA.URL_RELATIVA;
	
	    let urlParams = {
	        acao: event.currentTarget.dataset.acao,
	        page: event.currentTarget.dataset.page,
	        size: event.currentTarget.dataset.size
	    };
	
	    if (params != null) {
	        urlParams = { ...urlParams, ...params };
	    }
	    const result = await getRequest(urlRelativa, urlParams);
	
	    // Atualiza os elementos .lista
	    atualizaElementos(result, adicionador, elementFactory);
	}
	
    const addEventClick = () => {
		const indexCollection = document.querySelectorAll('.index');
        indexCollection.forEach(index => index.addEventListener('click', clickHandler));
    };

    return { addEventClick };
}
/**
 * Atualiza os elementos na seção de empresas no DOM.
 * - Cria novos elementos da lista usando um elementoFactory.
 * - Remove todos os elementos existente da lista.
 * - Captura um elemento de referência para inserção dos novos elementos.
 * - Insere os novos elementos antes do ponto de referência.
 * - Atribui eventos aos elementos atualizados usando um eventManager.
 *
 * @param {Object} empresas - Dados das empresas a serem exibidos.
 * @param {EventManager} eventManager - Gerenciador de eventos para associar eventos aos elementos.
 * @param {Object} elementFactory - Fábrica de elementos para criar novos elementos da lista.
 */
export function atualizaElementos(empresas, eventManager, elementFactory) {
	const containerEmpresas = document.querySelector('.container-empresas');
	
	//cria os novos elementos .lista
	const novosElementosLista = criaColecaoNovosElementosLi(empresas, elementFactory);
	
	//remove todos os elementos .lista
	const colecaoLista = document.querySelectorAll('.lista');
	colecaoLista.forEach(filho => containerEmpresas.removeChild(filho));
	
    //captura um elemento que será usado como ponto de referencia para inserção dos novos elementos .lista
	const referencePoint = containerEmpresas.querySelector('.paginacao');
	
	//insere todos os elementos os novos elementos .lista
	novosElementosLista.forEach(element => containerEmpresas.insertBefore(element, referencePoint));

    if (eventManager instanceof EventManager) {
        eventManager.assignEventsToTarget(containerEmpresas);
    }
}
function criaColecaoNovosElementosLi(result, elementFactory){
	const empresas = (result.empresas) ? result.empresas : result;
	const colecaoNovosLi = [];
	
	empresas.forEach(empresa => {
		let novoElement = elementFactory.createElement(empresa);
		colecaoNovosLi.push(novoElement);
	});
	return colecaoNovosLi;
}
