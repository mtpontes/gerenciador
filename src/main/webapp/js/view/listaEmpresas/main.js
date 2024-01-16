import { getRequest } from "../../util/ajax.js";
import { API_CONFIG } from "../../util/api-config.js";

document.addEventListener('DOMContentLoaded', clickEventPaginationtIndex);

function clickEventPaginationtIndex(){
	const indexCollection = document.querySelectorAll('.index');
	indexCollection.forEach((element) => {
		element.addEventListener('click', async (event) => {
			event.preventDefault();
			
			const urlRelativa = API_CONFIG.EMPRESA.URL_RELATIVA;
			const urlParams = {
				acao: element.dataset.acao,
				page: element.dataset.page,
				size: element.dataset.size
			}
			
			const result = await getRequest(urlRelativa, urlParams);
			//atualiza os elementos .lista
			atualizaElementos(result);
		});
	});
}

function atualizaElementos(empresas){
	const containerEmpresas = document.querySelector('.container-empresas');
	
	const novosElementosLista = criaColecaoNovosElementosLi(empresas);
	//remove todos os elementos .lista
	const colecaoLista = document.querySelectorAll('.lista');
	colecaoLista.forEach(filho => containerEmpresas.removeChild(filho));
	
    //ponto referencia
	const controladorPaginacaoReference = containerEmpresas.querySelector('.paginacao');
	//insere todos os elementos .lista atualizados
	novosElementosLista.forEach(element => containerEmpresas.insertBefore(element, controladorPaginacaoReference));
}

function criaColecaoNovosElementosLi(empresas){
	const colacaoNovosLi = [];
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
	return colacaoNovosLi;
}
