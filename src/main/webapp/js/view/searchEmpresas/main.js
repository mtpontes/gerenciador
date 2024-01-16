import { getRequest } from "../../util/ajax.js";
import { alteraDadosPaginacao, atualizaParamAcaoUrl } from "../../modules/pagination/paginationConfigs.js"
import { logicaPaginacao } from "../../modules/pagination/pagination.js";
import { API_CONFIG } from "../../util/api-config.js";

document.addEventListener("DOMContentLoaded", alteraRequisicao);
document.addEventListener("DOMContentLoaded", clickEventPaginationtIndex);
document.addEventListener("DOMContentLoaded", mostraMensagemFalhaPesquisa);

//atualiza o conteúdo do card e o controlador de paginação
function atualizaPagina(result){
	//interno
	mostraMensagemFalhaPesquisaRequest(result)
	atualizaElementos(result.empresas);
	
	//import
	atualizaParamAcaoUrl(result.acao);
	alteraDadosPaginacao(result.pagination);
	
	logicaPaginacao();
}

//substitui a requisição form HTML padrão por uma requisição AJAX
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

function clickEventPaginationtIndex(){
	const indexCollection = document.querySelectorAll('.index');
	indexCollection.forEach((element) => {
		element.addEventListener('click', async (event) => {
			event.preventDefault();
			
			const nomeEmpresa = getParamNomeEmpresaFromURL();
			const urlRelativa = API_CONFIG.EMPRESA.URL_RELATIVA;
			const urlParams = {
				acao: element.dataset.acao,
				page: element.dataset.page,
				size: element.dataset.size,
				nomeEmpresa: nomeEmpresa
			}
			
			const result = await getRequest(urlRelativa, urlParams);
			//atualiza os elementos .lista
			atualizaElementos(result.empresas);
		});
	});
}

function atualizaElementos(empresas){
	const containerEmpresas = document.querySelector('.container-empresas');
	
	//cria novos elementos .lista
	const novosElementosLista = criaColecaoNovosElementosLi(empresas);
	
	//remove todos os elementos .lista
	const colecaoLista = document.querySelectorAll('.lista');
	colecaoLista.forEach(filho => containerEmpresas.removeChild(filho));
	
    //captura um elemento que será usado como ponto de referencia para inserção dos novos elementos .lista
	const controladorPaginacaoReference = containerEmpresas.querySelector('.paginacao');
	
	//insere todos os elementos .lista atualizados
	novosElementosLista.forEach(element => containerEmpresas.insertBefore(element, controladorPaginacaoReference));
}

//mostra a mensagem de falha na pesquisa se nenhum elemento .lista for inserido
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

function criaColecaoNovosElementosLi(empresas){
	const colacaoNovosLi = [];
	
	if(empresas){
		empresas.forEach(empresa => {
	    const novoLi = document.createElement('li');
	    novoLi.classList.add('lista');
	    novoLi.id = empresa.id;
	    
	    const paragrafoNome = document.createElement('p');
	    paragrafoNome.classList.add('lista-nome');
	    paragrafoNome.id = 'lista-nome';
	    paragrafoNome.textContent = empresa.nome;
	
	    const paragrafoData = document.createElement('p');
	    paragrafoData.classList.add('lista-data');
	    paragrafoData.id = 'lista-data';
	    paragrafoData.textContent = empresa.data;
	    
	    novoLi.appendChild(paragrafoNome);
	    novoLi.appendChild(paragrafoData);
    
    	colacaoNovosLi.push(novoLi);
		});
	}
	return colacaoNovosLi;
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

