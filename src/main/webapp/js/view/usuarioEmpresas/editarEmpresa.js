import { putRequest } from "../../util/ajaxUtil.js";
import { API_CONFIG } from "../../util/api-config.js";

const P_NOME_SELECTOR = '.lista-nome';
const P_DATA_SELECTOR = '.lista-data';
const INPUT_NOME_SELECTOR = '.entrada-nome';
const INPUT_DATA_SELECTOR = '.entrada-data';

/**
 * Adiciona um evento de clique a uma coleção de botões de edição.
 * Ao clicar no botão, previne o comportamento padrão do formulário, captura o formulário pai e realiza ações específicas.
 * 
 * @param {NodeList} collection - Coleção de botões aos quais o evento será adicionado.
 */
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

/**
 * Adiciona um evento de submissão a uma coleção de formulários de edição.
 * Ao submeter o formulário, previne o comportamento padrão, recupera os valores dos campos input e realiza uma requisição de atualização.
 * Após a atualização, esconde os campos de input e atualiza os elementos <p> correspondentes com os novos dados.
 * 
 * @param {NodeList} collection - Coleção de formulários aos quais o evento será adicionado.
 */
export function eventoSubmitElementoFormLista(collection) {
    collection.forEach(form => {
        form.addEventListener('submit', async (event) => {
            event.preventDefault();

            // Constrói a URL relativa com o parâmetro de ação
            const relativeURL = API_CONFIG.EMPRESA.URL_RELATIVA;
            const paramAcao = API_CONFIG.getParamAcao(API_CONFIG.EMPRESA.PARAM_ACAO.ATUALIZA);
            const relativeURLWithParamAcao = relativeURL + paramAcao;

            // Recupera o texto inserido nos campos input (nome e data)
            const nome = form.querySelector(INPUT_NOME_SELECTOR).value;
            const data = form.querySelector(INPUT_DATA_SELECTOR).value;

            // O ID de cada elemento .lista é igual ao ID da Empresa na API
            const empresaId = form.dataset.empresaid;

            // Corpo da requisição
            const requestBody = {
                nome: nome,
                data: data,
                id: empresaId
            }

            // Realiza uma requisição PUT para atualizar os dados da Empresa
            const response = await putRequest(relativeURLWithParamAcao, requestBody);

            // Confirma se a requisição foi bem-sucedida
            const success = response.ok;
            if (success) {
                // Esconde os campos de input ao submeter o formulário
                const elementos = capturaElementos(form);
                ocultaCamposInput(elementos);

                // Atualiza os elementos <p> correspondentes com os novos dados
                atualizaElementosP(elementos, success);
            }
        });
    });
}

/**
 * Altera a exibição dos campos de input em um formulário.
 * Se os campos estiverem ocultos, os exibe, atualiza seus valores e os deixa em foco.
 * Se os campos estiverem visíveis, os oculta.
 * 
 * @param {HTMLFormElement} elementoForm - O formulário cujos campos de input serão alterados.
 */
function alteraExibicaoDosElementosPEInput(elementoForm){
	const elementos = capturaElementos(elementoForm);
	
	if(!camposInputIsVisiveis(elementos.input)){
		mostraCamposInput(elementos);
		atualizaElementosInput(elementos);
	} else {
		ocultaCamposInput(elementos);
	}
}

// Captura o texto atual dos elementos <p> e introduz no campo <input>
function atualizaElementosInput(elementos){
	elementos.input.nome.value = elementos.p.nome.textContent;
	elementos.input.data.value = elementos.p.data.textContent;
}

/**
 * Atualiza os elementos de parágrafo (<p>) com os valores dos campos de input em caso de sucesso na requisição AJAX.
 * 
 * @param {Object} elementos - Objeto contendo referências para os elementos do formulário.
 * @param {boolean} success - Indica se a requisição AJAX foi bem-sucedida.
 */
function atualizaElementosP(elementos, success){
	if(success){
		elementos.p.nome.textContent = elementos.input.nome.value;
		elementos.p.data.textContent = elementos.input.data.value;
	}
}

// Retorna um objeto que guarda os elementos <p> e <input>
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

// Verifica se os campos <input> estão visiveis
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
