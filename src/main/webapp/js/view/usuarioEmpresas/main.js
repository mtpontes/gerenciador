import { API_CONFIG } from "../../util/api-config.js";
import { getRequest } from "../../util/ajax.js";

import { elementFactory } from "./elementFactory.js";
import { eventArchiveUnarquive } from "./botoes.js";
import { eventoClickElementoEditar, eventoSubmitElementoFormLista } from "./editarEmpresa.js";

import { alteraDadosPaginacao, atualizaParamAcaoUrl, clickEventPaginationtIndex, atualizaElementos } from "../../modules/pagination/paginationConfigs.js";
import { logicaPaginacao } from "../../modules/pagination/pagination.js";

import { atualizaEstiloArquivados } from "./botoes.js";
import { atualizaIconeBotaoArquivar } from "./botoes.js";

import { EventManager } from "../../util/eventManagerUtil.js";

document.addEventListener('DOMContentLoaded', eventoSubmitElementoFormLista(document.querySelectorAll('.lista')));
document.addEventListener('DOMContentLoaded', eventoClickElementoEditar(document.querySelectorAll('.botao-editar')));
document.addEventListener('DOMContentLoaded', eventArchiveUnarquive(document.querySelectorAll('.botao-arquivar')));
document.addEventListener('DOMContentLoaded', paginationEvent);
document.addEventListener('DOMContentLoaded', alternaEmpresasAtivo);
document.addEventListener('DOMContentLoaded', atualizaEstiloArquivados);
document.addEventListener('DOMContentLoaded', atualizaIconeBotaoArquivar);

// Atualiza o conteúdo do card e o controlador de paginação
function atualizaPagina(result){
	atualizaElementos(result.empresas);
	atualizaParamAcaoUrl(result.acao);
	alteraDadosPaginacao(result.pagination);
	logicaPaginacao();
}

/**
 * Faz chamadas por registros de `Empresa` `ativo==true` e `ativo==false`.
 *
 * @description Esta função é vinculada a um evento de clique no botão '.arquivados'.
 *              Ela alterna entre a exibição de empresas ativas e inativas, modificando
 *              o parâmetro 'acao' da URL e realizando chamadas à API correspondentes.
 *              Após a chamada, atualiza a página e aplica estilos ao botão '.arquivados'.
 */
function alternaEmpresasAtivo(){
	const botaoArquivados = document.querySelector('.arquivados');
	botaoArquivados.addEventListener('click', async () => {
		const relativeURL = API_CONFIG.EMPRESA.URL_RELATIVA;
		
		// Recupera os pâmetros da URL atual
		const searchParams = new URLSearchParams(window.location.search);
		
		// Alterna o valor do parâmetro `acao` da URL
		const ativadas = API_CONFIG.EMPRESA.PARAM_ACAO.LISTA_EMPRESAS_USUARIO;
		const desativadas = API_CONFIG.EMPRESA.PARAM_ACAO.LISTA_EMPRESAS_DESATIVADAS_USUARIO;
		(searchParams.get('acao') === desativadas) 
			? searchParams.set('acao', ativadas) 
			: searchParams.set('acao', desativadas);
		
		// Transforma searchParams em um objeto literal, cada parametro vira chave:valor no objeto
		const params = Object.fromEntries(searchParams.entries());

		const result = await getRequest(relativeURL, params);
		atualizaPagina(result);
		atualizaEstiloArquivados();
	});
}

/**
 * Controla a lógica de paginação ao clicar em índices de página.
 * - Cria um novo EventManager para gerenciar eventos na página.
 * - Adiciona objetos de evento ao EventManager para os elementos específicos.
 * - Chama a função de paginação ao clicar nos índices de página.
 */
function paginationEvent(){
    const atribuidorEventFunctions = new EventManager();
    
    atribuidorEventFunctions
    	.associateTargetAndEvent('.lista', eventoSubmitElementoFormLista)
    	.associateTargetAndEvent('.botao-editar', eventoClickElementoEditar)
    	.associateTargetAndEvent('.botao-arquivar', eventArchiveUnarquive);

    clickEventPaginationtIndex(null, atribuidorEventFunctions, elementFactory).addEventClick();
}

