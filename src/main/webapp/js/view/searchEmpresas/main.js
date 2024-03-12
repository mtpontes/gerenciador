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
function atualizaPagina(result, elementFactory){
	// Interno
	mostraMensagemFalhaPesquisaAJAXRequest(result)
	paginationEventUpdate();
	
	// Import
	atualizaElementos(result, null, elementFactory);
	clickEventIndexCollection();
	alteraDadosPaginacao(result.pagination);
	
	logicaPaginacao(API_CONFIG.EMPRESA.PARAM_ACAO.SEARCH);
}

/**
 * Substitui a requisição form HTML padrão da searchBar por uma requisição AJAX.
 * 
 * @description Esta função é vinculada ao evento de envio do formulário da barra de pesquisa.
 *              Ela impede o comportamento padrão do formulário, obtém o nome da empresa do input
 *              do formulário, realiza uma requisição AJAX para buscar empresas com base no nome
 *              fornecido e, em seguida, atualiza a página com os dados obtidos na resposta da requisição.
 */
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
        const elementFactory = new SearchEmpresasElementFactory();

        // Atualiza a página com os dados obtidos na resposta da requisição
        atualizaPagina(result, elementFactory);
    });
}

/**
 * Adiciona eventos de paginação à lista de empresas.
 * Obtém o nome da empresa a partir da URL e utiliza como parâmetro na criação dos eventos de paginação.
 * 
 * @description Esta função configura eventos de paginação para a lista de empresas. 
 *              Ela extrai o nome da empresa da URL e utiliza como parâmetro na criação dos eventos.
 *              Além disso, instancia um `ElementFactory` específico para a página de pesquisa de empresas.
 */
function paginationEvent() {
    // Obtém o nome da empresa a partir da URL
    let params = { nomeEmpresa: getParamNomeEmpresaFromURL() };

    // Instancia seu ElementFactory específico para a página de pesquisa de empresas
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


/**
 * Mostra uma mensagem de falha na pesquisa caso não haja elementos .lista na página.
 * 
 * @description Esta função verifica se há elementos com a classe .lista na página. 
 *              Se não houver, exibe uma mensagem de falha na pesquisa, oculta o card de resultados 
 *              e exibe uma mensagem indicando a falta de resultados. Caso contrário, restaura a exibição 
 *              do card e oculta a mensagem de falha na pesquisa.
 */
function mostraMensagemFalhaPesquisaAoCarregarPagina() {
    const colecaoLista = Array.from(document.querySelectorAll('.lista'));
    const mensagem = document.querySelector('.listagemVazia');
    const card = document.querySelector('.card');

    if (colecaoLista.length === 0) {
        card.classList.add('card-off');
        mensagem.style.display = 'flex';
    } else {
        card.classList.remove('card-off');
        mensagem.style.display = 'none';
    }
}

/**
 * Mostra uma mensagem de falha na pesquisa caso o retorno do AJAX não contenha dados de `Empresa` no JSON.
 * 
 * @param {Object} result - O objeto resultante da requisição AJAX.
 * @description Esta função verifica se o objeto result contém o atributo "empresas" e se o array associado a esse 
 *              atributo está vazio. Caso afirmativo, exibe uma mensagem de falha na pesquisa, oculta o card de resultados 
 *              e exibe uma mensagem indicando a falta de resultados. Caso contrário, restaura a exibição do card e 
 *              oculta a mensagem de falha na pesquisa.
 */
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
