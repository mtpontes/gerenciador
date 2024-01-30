import { getRequest } from "../../util/ajaxUtil.js";
import { alteraDadosPaginacao, clickEventPaginationtIndex, atualizaElementos } from "../../modules/pagination/paginationConfigs.js"
import { logicaPaginacao } from "../../modules/pagination/pagination.js";
import { clickEventIndexCollection } from "../../modules/pagination/eventosPagina.js";
import { API_CONFIG } from "../../util/api-config.js";
import { SearchEmpresasElementFactory } from "../searchEmpresas/SearchEmpresasElementFactory.js";

document.addEventListener("DOMContentLoaded", alteraRequisicao);
document.addEventListener("DOMContentLoaded", paginationEvent);
document.addEventListener("DOMContentLoaded", mostraMensagemFalhaPesquisaAoCarregarPagina);

// Atualiza o conteúdo do card e o controlador de paginação
function atualizaPagina(result){
	// Interno
	mostraMensagemFalhaPesquisaAJAXRequest(result)
	paginationEventUpdate();
	
	// Import
	atualizaElementos(result);
	clickEventIndexCollection();
	alteraDadosPaginacao(result.pagination);
	
	logicaPaginacao();
}

// Substitui a requisição form HTML padrão da searchBar por uma requisição AJAX
async function alteraRequisicao() {
    const formulario = document.getElementById('form-search');
    formulario.addEventListener('submit', async (event) => {
        event.preventDefault();

        // Obtém o nome da empresa do input do formulário
        const nomeEmpresa = getParamNomeEmpresaFromInput();

        // Configuração da URL relativa e dos parâmetros da requisição
        const urlRelativa = API_CONFIG.EMPRESA.URL_RELATIVA;
        const params = {
            nomeEmpresa: nomeEmpresa,
            acao: API_CONFIG.EMPRESA.PARAM_ACAO.SEARCH
        };
        const result = await getRequest(urlRelativa, params);

        // Atualiza a página com os dados obtidos na resposta da requisição
        atualizaPagina(result);
    });
}

/**
 * Adiciona eventos de paginação à lista de empresas.
 * Obtém o nome da empresa a partir da URL e utiliza como parâmetro na criação dos eventos de paginação.
 */
function paginationEvent() {
    // Obtém o nome da empresa a partir da URL
    let params = { nomeEmpresa: getParamNomeEmpresaFromURL() };

	// Instancia seu ElementFactory
	let elementFactory = new SearchEmpresasElementFactory();

    // Cria um evento de paginação com os parâmetros configurados e o ElementFactory
    const paginationEvent = clickEventPaginationtIndex(params, null, elementFactory);
    paginationEvent.addEventClick();
}
/**
 * Atualiza os eventos de paginação.
 * Apaga todos os eventos vinculados aos elementos `.index` usando clonagem.
 * Em seguida, chama a função `paginationEvent` para adicionar os eventos aos novos elementos de índice.
 * 
 * @note - Apenas remover os eventos com `removeEventListener` não funcionou.
 */
function paginationEventUpdate() {
    // Seleciona todos os elementos de índice de paginação
    const indexCollection = document.querySelectorAll('.index');

    // Para cada elemento de índice, cria um clone e substitui o original pelo clone
    indexCollection.forEach(index => {
        const indexClone = index.cloneNode(true);
        index.parentNode.replaceChild(indexClone, index);
    });

    // Chama a função para adicionar eventos de paginação aos novos elementos de índice
    paginationEvent();
}


// Mostra mensagem de falha na pesquisa caso não haja elementos .lista na página
function mostraMensagemFalhaPesquisaAoCarregarPagina(){
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
// Mostra mensagem de falha na pesquisa caso o retorno do AJAX não tenha dados de `Empresa` no Json
function mostraMensagemFalhaPesquisaAJAXRequest(result){
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

// Recupera o valor do parâmetro "nomeEmpresa" da URL
function getParamNomeEmpresaFromURL() {
	return new URLSearchParams(window.location.search).get('nomeEmpresa');
}
// Recupera o texto digitado no campo input da barra de pesquisa
function getParamNomeEmpresaFromInput() {
    const searchInputValue = document.querySelector('.search-input').value;
    const searchInputPlaceholder = document.querySelector('.search-input').placeholder;
    const nomeEmpresa = searchInputValue ? searchInputValue : searchInputPlaceholder;
    return nomeEmpresa;
}

