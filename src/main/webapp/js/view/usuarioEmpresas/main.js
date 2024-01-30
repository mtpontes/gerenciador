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

//atualiza o conteúdo do card e o controlador de paginação
function atualizaPagina(result){
	atualizaElementos(result.empresas);
	atualizaParamAcaoUrl(result.acao);
	alteraDadosPaginacao(result.pagination);
	logicaPaginacao();
}

function alternaEmpresasAtivo(){
	const botaoArquivados = document.querySelector('.arquivados');
	botaoArquivados.addEventListener('click', async () => {
		const relativeURL = API_CONFIG.EMPRESA.URL_RELATIVA;
		
		//recupera os pâmetros da URL atual
		const searchParams = new URLSearchParams(window.location.search);
		
		//alterna o valor do parâmetro 'acao' da URL
		const ativadas = API_CONFIG.EMPRESA.PARAM_ACAO.LISTA_EMPRESAS_USUARIO;
		const desativadas = API_CONFIG.EMPRESA.PARAM_ACAO.LISTA_EMPRESAS_DESATIVADAS_USUARIO;
		(searchParams.get('acao') === desativadas) ? searchParams.set('acao', ativadas) : searchParams.set('acao', desativadas);
		
		//transforma searchParams em um objeto literal, cada parametro vira chave:valor no objeto
		const params = Object.fromEntries(searchParams.entries());

		const result = await getRequest(relativeURL, params);
		atualizaPagina(result);
		atualizaEstiloArquivados();
	});
}

function paginationEvent(){
    const adicionador = new EventManager();
    
    adicionador
    	.criaObjeto('.lista', eventoSubmitElementoFormLista)
    	.criaObjeto('.botao-editar', eventoClickElementoEditar)
    	.criaObjeto('.botao-arquivar', eventArchiveUnarquive);

    clickEventPaginationtIndex(null, adicionador, elementFactory).addEventClick();
}

