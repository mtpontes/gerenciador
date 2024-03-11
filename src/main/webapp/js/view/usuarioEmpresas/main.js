import { API_CONFIG } from "../../util/api-config.js";
import { getRequest } from "../../util/ajaxUtil.js";

import { UsuarioEmpresasElementFactory } from "./UsuarioEmpresasElementFactory.js";
import { eventArchiveUnarquive } from "./botoes.js";
import { eventoClickElementoEditar, eventoSubmitElementoFormLista } from "./editarEmpresa.js";

import { alteraDadosPaginacao, clickEventPaginationtIndex, atualizaElementos } from "../../modules/pagination/paginationConfigs.js";
import { logicaPaginacao } from "../../modules/pagination/pagination.js";

import { atualizaEstiloArquivados } from "./botoes.js";
import { atualizaIconeBotaoArquivar } from "./botoes.js";

import { EventManagerUtil } from "../../util/EventManagerUtil.js";

document.addEventListener('DOMContentLoaded', eventoSubmitElementoFormLista(document.querySelectorAll('.lista')));
document.addEventListener('DOMContentLoaded', eventoClickElementoEditar(document.querySelectorAll('.botao-editar')));
document.addEventListener('DOMContentLoaded', eventArchiveUnarquive(document.querySelectorAll('.botao-arquivar')));
document.addEventListener('DOMContentLoaded', paginationEvent);
document.addEventListener('DOMContentLoaded', alternaEmpresasAtivo);
document.addEventListener('DOMContentLoaded', atualizaEstiloArquivados);
document.addEventListener('DOMContentLoaded', atualizaIconeBotaoArquivar);
//document.addEventListener('DOMContentLoaded', atualizaParamAtivo);

/**
 * Atualiza o conteúdo do card e o controlador de paginação.
 *
 * @param {Object} result - Objeto contendo informações após uma chamada à API.
 * @param {Object} eventManager - Instância do controlador de eventos.
 * @param {Object} elementFactory - Instância da fábrica de elementos.
 */
function atualizaPagina(result, eventManager, elementFactory) {
    atualizaElementos(result, eventManager, elementFactory);
    alteraDadosPaginacao(result.pagination);
    logicaPaginacao();
}


/**
 * Realiza chamadas para registros de `Empresa` com `ativo==true` e `ativo==false`.
 *
 * @description Esta função é vinculada a um evento de clique no botão '.arquivados'.
 *              Ela alterna entre a exibição de empresas ativas e inativas, modificando
 *              o parâmetro 'acao' da URL e realizando chamadas à API correspondentes.
 *              Após a chamada, atualiza a página e aplica estilos ao botão '.arquivados'.
 */
function alternaEmpresasAtivo() {
    const botaoArquivados = document.querySelector('.arquivados');
    botaoArquivados.addEventListener('click', async () => {
        const relativeURL = API_CONFIG.EMPRESA.URL_RELATIVA;

        // Recupera os parâmetros da URL atual
        const searchParams = new URLSearchParams(window.location.search);

        // Altera o endpoint para onde a requisição irá
        searchParams.set("acao", API_CONFIG.EMPRESA.PARAM_ACAO.LISTA_EMPRESAS_USUARIO)
        
        // Alterna o valor do parâmetro "ativo" entre true e false
        if(searchParams.get("ativo") !== null && searchParams.get("ativo") !== undefined){
			console.log("morreu no if");
			searchParams.set("ativo", searchParams.get("ativo") === "true" ? false : true);
		} else {
			console.log("morreu no else");
			searchParams.set("ativo", false);
		}
		console.log("O valor é ", searchParams.get("ativo"));

        // Transforma searchParams em um objeto literal, cada parâmetro vira chave:valor no objeto
        const params = Object.fromEntries(searchParams.entries());

        const result = await getRequest(relativeURL, params);

        const eventManager = criaEventManager();
        const elementFactory = new UsuarioEmpresasElementFactory();

        atualizaPagina(result, eventManager, elementFactory);
        atualizaParamAtivo(params.ativo);
        atualizaEstiloArquivados();
    });
}

function atualizaParamAtivo(ativo){
    const urlAtual = new URL(window.location.href);
    urlAtual.searchParams.set('ativo', ativo);
    history.pushState(null, '', urlAtual.href);
}


/**
 * Controla a lógica de paginação ao clicar em índices de página.
 * - Cria um novo EventManager para gerenciar eventos na página.
 * - Adiciona objetos de evento ao EventManager para os elementos específicos.
 * - Chama a função de paginação ao clicar nos índices de página.
 */
function paginationEvent(){
    const atribuidorEventFunctions = criaEventManager();
    const elementFactory = new UsuarioEmpresasElementFactory();

    clickEventPaginationtIndex(null, atribuidorEventFunctions, elementFactory).addEventClick();
}

function criaEventManager(){
    const atribuidorEventFunctions = new EventManagerUtil();

	atribuidorEventFunctions
		.associateTargetAndEvent('.lista', eventoSubmitElementoFormLista)
		.associateTargetAndEvent('.botao-editar', eventoClickElementoEditar)
		.associateTargetAndEvent('.botao-arquivar', eventArchiveUnarquive);
		
	return atribuidorEventFunctions;
}

