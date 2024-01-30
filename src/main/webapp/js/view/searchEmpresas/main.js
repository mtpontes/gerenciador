import { getRequest } from "../../util/ajax.js";
import { alteraDadosPaginacao, clickEventPaginationtIndex, atualizaElementos } from "../../modules/pagination/paginationConfigs.js"
import { logicaPaginacao } from "../../modules/pagination/pagination.js";
import { clickEventIndexCollection } from "../../modules/pagination/eventosPagina.js";
import { API_CONFIG } from "../../util/api-config.js";
import { elementFactory } from "../searchEmpresas/elementFactory.js";

document.addEventListener("DOMContentLoaded", alteraRequisicao);
document.addEventListener("DOMContentLoaded", paginationEvent);
document.addEventListener("DOMContentLoaded", mostraMensagemFalhaPesquisa);

//atualiza o conteúdo do card e o controlador de paginação
function atualizaPagina(result){
	//interno
	mostraMensagemFalhaPesquisaRequest(result)
	paginationEventUpdate();
	
	//import
	atualizaElementos(result);
	clickEventIndexCollection();
	alteraDadosPaginacao(result.pagination);
	
	logicaPaginacao();
}

//substitui a requisição form HTML padrão da searchBar por uma requisição AJAX
async function alteraRequisicao(){
	const formulario = document.getElementById('form-search');
	formulario.addEventListener('submit', async (event) => {
		event.preventDefault();

		const nomeEmpresa = getParamNomeEmpresaFromInput();
		const urlRelativa = API_CONFIG.EMPRESA.URL_RELATIVA;		
		const params = {
			nomeEmpresa: nomeEmpresa,
			acao: API_CONFIG.EMPRESA.PARAM_ACAO.SEARCH
		};
		
		const result = await getRequest(urlRelativa, params);
		atualizaPagina(result);
	});
}
function paginationEvent(){
	let params = {nomeEmpresa: getParamNomeEmpresaFromURL()};
	const paginationEvent = clickEventPaginationtIndex(params, null, elementFactory);
	paginationEvent.addEventClick();
}
function paginationEventUpdate() {
	const indexCollection = document.querySelectorAll('.index');
	
	indexCollection.forEach(index => {
		const indexClone = index.cloneNode(true);
		index.parentNode.replaceChild(indexClone, index);
	});
	paginationEvent();
}


function mostraMensagemFalhaPesquisa(){
	const colecaoLista = Array.from(document.querySelectorAll('.lista'));
	const mensagem = document.querySelector('.listagemVazia');
	const card = document.querySelector('.card');

	if(colecaoLista.length === 0){
		card.classList.add('card-off');
		mensagem.style.display = 'flex';
	} else {
		card.classList.remove('card-off');
		mensagem.style.display = 'none';
	}
}
function mostraMensagemFalhaPesquisaRequest(result){
	const mensagem = document.querySelector('.listagemVazia');
	const card = document.querySelector('.card');
	
	//verifica se existe o atributo "empresas" no objeto
	if('empresas' in result){
		//verifica se o tamanho do array "empresas" é vazio
		if(Array.isArray(result.empresas) && result.empresas.length === 0){
			card.classList.add('card-off');
			mensagem.style.display = 'flex';
		} else {
			card.classList.remove('card-off');
			mensagem.style.display = 'none';
		}
	} else {
		card.classList.add('card-off');
		mensagem.style.display = 'flex';
	}
}

function getParamNomeEmpresaFromURL() {
	return new URLSearchParams(window.location.search).get('nomeEmpresa');
}
function getParamNomeEmpresaFromInput() {
    const searchInputValue = document.querySelector('.search-input').value;
    const searchInputPlaceholder = document.querySelector('.search-input').placeholder;
    const nomeEmpresa = searchInputValue ? searchInputValue : searchInputPlaceholder;
    return nomeEmpresa;
}

