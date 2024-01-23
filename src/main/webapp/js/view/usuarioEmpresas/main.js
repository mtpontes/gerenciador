import { getRequest, putRequest } from "../../util/ajax.js";
import { criaColecaoNovosElementosLi } from "./criaColecaoNovosElementosLi.js";
import { alteraDadosPaginacao, atualizaParamAcaoUrl } from "../../modules/pagination/paginationConfigs.js";
import { logicaPaginacao } from "../../modules/pagination/pagination.js";
import { atualizaEstiloArquivados } from "./botoes.js";
import { eventoClickElementoEditar, eventoSubmitElementoFormLista } from "./editarEmpresa.js";
import { atualizaIconeBotaoArquivar } from "./botoes.js";
import { API_CONFIG } from "../../util/api-config.js";

document.addEventListener('DOMContentLoaded', eventoSubmitElementoFormLista(document.querySelectorAll('.lista')));
document.addEventListener('DOMContentLoaded', eventoClickElementoEditar(document.querySelectorAll('.botao-editar')));
document.addEventListener('DOMContentLoaded', eventArchiveUnarquive(document.querySelectorAll('.botao-arquivar')));
document.addEventListener('DOMContentLoaded', clickEventPaginationtIndex);
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
//		atualizaIconeBotaoArquivar();
	});
}

function eventArchiveUnarquive(collection){
	collection.forEach(button => {
		button.addEventListener('click', async (event) => {
			event.preventDefault();
			//captura o pai do elemento pai de button (.lista)
			const elementoLiLista = (button.parentNode).parentNode;
			//captura o pai do elemento .lista (.container-empresas)
			const containerEmpresas = elementoLiLista.parentNode;
			
			const relativeURL = API_CONFIG.getUrlRelativaComParamAcao(API_CONFIG.EMPRESA.PARAM_ACAO.ARQUIVA);
			const empresaId = button.dataset.empresaid;
			let requestBody = {
				empresaId: empresaId
			};
			const response =  await putRequest(relativeURL, requestBody);
			
			//se a requisição der certo
			if(response.ok){
				//remove o elemento atual que representa o objeto Empresa (.lista)
				containerEmpresas.removeChild(elementoLiLista);
			}		
		});
	});
}

function clickEventPaginationtIndex(){
	const indexCollection = document.querySelectorAll('.index');
	indexCollection.forEach((element) => {
		element.addEventListener('click', async (evento) => {
			evento.preventDefault();
			
			const urlRelativa = API_CONFIG.EMPRESA.URL_RELATIVA;
			const urlParams = {
				acao: element.dataset.acao,
				page: element.dataset.page,
				size: element.dataset.size
			}
			
			const result = await getRequest(urlRelativa, urlParams);
			//atualiza os elementos .lista
			atualizaElementos(result.empresas);
		});
	});
}


function atualizaElementos(empresas){
	const containerEmpresas = document.querySelector('.container-empresas');
	
	//cria os novos elementos .lista
	const novosElementosLista = criaColecaoNovosElementosLi(empresas);
	
	//remove todos os elementos .lista atuais
	const colecaoLista = document.querySelectorAll('.lista');
	colecaoLista.forEach(filho => containerEmpresas.removeChild(filho));
	
    //captura um elemento que será usado como ponto de referencia para inserção dos novos elementos .lista
	const controladorPaginacaoReference = containerEmpresas.querySelector('.paginacao');
	
	//insere todos os elementos os novos elementos .lista
	novosElementosLista.forEach(element => {
		containerEmpresas.insertBefore(element, controladorPaginacaoReference)
	});
	
	//adiciona os eventos que foram perdidos com a manipulação dos elementos .lista
	const formListaCollection = containerEmpresas.querySelectorAll('.lista');
	const buttonEditCollection = containerEmpresas.querySelectorAll('.botao-editar');
	const buttonArchiveCollection = containerEmpresas.querySelectorAll('.botao-arquivar');
	eventoSubmitElementoFormLista(formListaCollection);
	eventoClickElementoEditar(buttonEditCollection);
	eventArchiveUnarquive(buttonArchiveCollection);
}

