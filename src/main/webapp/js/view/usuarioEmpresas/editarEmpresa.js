import { putRequest } from "../../util/ajax.js";
import { API_CONFIG } from "../../util/api-config.js";

const P_NOME_SELECTOR = '.lista-nome';
const P_DATA_SELECTOR = '.lista-data';
const INPUT_NOME_SELECTOR = '.entrada-nome';
const INPUT_DATA_SELECTOR = '.entrada-data';

export function eventoClickElementoEditar(collection){
	collection.forEach(button => {
		button.addEventListener('click', (event) => {
			event.preventDefault();
			//através do botão 'editar', captura o elemento pai <form>
			const elementoForm = (button.parentNode).parentNode;
			//exibe os <inputs> e oculta os elementos <p>
			alteraExibicaoDosElementosPEInput(elementoForm);
			//deixa o elemento input .entrada-nome em foco
			elementoForm.querySelector(INPUT_NOME_SELECTOR).focus();
		});
	});
}

export function eventoSubmitElementoFormLista(collection){
	collection.forEach(form => {
		form.addEventListener('submit', async (event) => {
			event.preventDefault();
	
			const relativeURL = API_CONFIG.EMPRESA.URL_RELATIVA;
			const paramAcao = API_CONFIG.getParamAcao(API_CONFIG.EMPRESA.PARAM_ACAO.ATUALIZA);
			const relativeURLWithParamAcao = relativeURL + paramAcao;
			
			//recupera o texto que foi inserido nos campos input (nome e data)
			const nome = form.querySelector(INPUT_NOME_SELECTOR).value;
			const data = form.querySelector(INPUT_DATA_SELECTOR).value;
			//o ID de cada elemento .lista é == Empresa.id
			const empresaId = form.dataset.empresaid;
			
			const requestBody = {
				nome: nome,
				data: data,
				id: empresaId
			}
			const response = await putRequest(relativeURLWithParamAcao, requestBody);
			
			//confirma se a requisicao deu certo
			const success = response.ok;
			if(success){
				//esconde os campos de input ao submeter o formulário
				const elementos = capturaElementos(form);
				ocultaCamposInput(elementos);
				atualizaElementosP(elementos, success);
			}
		});
	});
}

function alteraExibicaoDosElementosPEInput(elementoForm){
	const elementos = capturaElementos(elementoForm);
	
	if(!camposInputIsVisiveis(elementos.input)){
		mostraCamposInput(elementos);
		atualizaElementosInput(elementos);
	} else {
		ocultaCamposInput(elementos);
	}
}

//captura o texto a ser exibido no elemento <p> e introduz no campo <input>
function atualizaElementosInput(elementos){
	elementos.input.nome.value = elementos.p.nome.textContent;
	elementos.input.data.value = elementos.p.data.textContent;
}

//usar somente em caso de sucesso na requisição AJAX
//captura o texto digitado no elemento <input> e introduz no elemento <p>
function atualizaElementosP(elementos, success){
	if(success){
		elementos.p.nome.textContent = elementos.input.nome.value;
		elementos.p.data.textContent = elementos.input.data.value;
	}
}

//retorna um objeto que guarda os elementos <p> e <input>
function capturaElementos(elementoForm){
	return {
		p: {
			nome: elementoForm.querySelector(P_NOME_SELECTOR),
			data: elementoForm.querySelector(P_DATA_SELECTOR)
		},
	
		input: { 
			nome: elementoForm.querySelector(INPUT_NOME_SELECTOR),
			data: elementoForm.querySelector(INPUT_DATA_SELECTOR)
		}
	};
}

//verifica se os campos <input> estão visiveis
function camposInputIsVisiveis(campos){
	return (campos.nome.style.display === 'block' && campos.data.style.display === 'block');
}

function mostraCamposInput(elementos){
	elementos.p.nome.style.display = elementos.p.data.style.display = 'none';
	elementos.input.nome.style.display = elementos.input.data.style.display = 'block';
}
function ocultaCamposInput(elementos){
	elementos.p.nome.style.display = elementos.p.data.style.display = 'block';
	elementos.input.nome.style.display = elementos.input.data.style.display = 'none';
}
