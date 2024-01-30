import { cssColors } from "../../util/variaveisCSS.js";
import { putRequest } from "../../util/ajax.js";

/**
 * Obtém o estado atual da página com base no parâmetro de "acao" da URL.
 * Retorna `true` se o parâmetro de ação for 'listaEmpresasDesativadasUsuario', caso contrário, retorna `false`.
 * 
 * @returns {boolean} - Estado da página.
 */
function getState() {
    const acao = new URL(window.location.href).searchParams.get('acao');
    return (acao === 'listaEmpresasDesativadasUsuario') ? true : false;
}


// Muda o estilo do botao 'arquivados'
export function atualizaEstiloArquivados() {
	const state = getState();
    const colors = cssColors();
    const arquivados = document.getElementById('arquivados');

	// Altera estilo do botao    
	arquivados.style.color = state ? colors.corPrincipal : colors.corBranca;
	arquivados.style.backgroundColor = state ? colors.corTerciaria : 'none';

    arquivados.style.transition = 'background-color 0.3s';

    // Emula o comportamento da subclase hover pois ele se perde quando o estilo é manipulado
    arquivados.addEventListener('mouseover', function() {
        arquivados.style.color = colors.corPrincipal;
        arquivados.style.backgroundColor = colors.corBranca;
    });
    arquivados.addEventListener('mouseout', function() {
        arquivados.style.color = state ? colors.corPrincipal : colors.corBranca;
        arquivados.style.backgroundColor = state ? colors.corTerciaria : colors.corSecundaria;
    });
}

export function atualizaIconeBotaoArquivar() {
	const state = getState();
	const botaoArquivarCollection = document.querySelectorAll('.botao-arquivar');

	botaoArquivarCollection.forEach(botao => {
		const textoBotaoArquivar = botao.querySelector('.texto-arquiva');
		const iconeBotaoArquivar = botao.querySelector('.icone-arquiva');
		
		textoBotaoArquivar.textContent = state ? 'Desarquivar' : 'Arquivar';
		iconeBotaoArquivar.textContent = state ? 'unarchive' : 'archive';
	});
}

/**
 * Adiciona um evento de arquivar/desarquivar a uma coleção de botões.
 * Ao clicar no botão, faz uma requisição para arquivar ou desarquivar a empresa associada e atualiza a interface.
 * 
 * @param {NodeList} collection - Coleção de botões aos quais o evento será adicionado.
 */
export function eventArchiveUnarquive(collection){
	collection.forEach(button => {
		button.addEventListener('click', async (event) => {
			event.preventDefault();
			// Captura o pai do elemento pai (.lista) do button 
			const elementoLiLista = (button.parentNode).parentNode;
			// Captura o pai (.container-empresas) do  elemento .lista
			const containerEmpresas = elementoLiLista.parentNode;
			
			const relativeURL = API_CONFIG.getUrlRelativaComParamAcao(API_CONFIG.EMPRESA.PARAM_ACAO.ARQUIVA);
			const empresaId = button.dataset.empresaid;
			let requestBody = {
				empresaId: empresaId
			};
			const response =  await putRequest(relativeURL, requestBody);
			
			// Se a requisição der certo
			if(response.ok){
				// Remove o elemento atual que representa o objeto Empresa (.lista)
				containerEmpresas.removeChild(elementoLiLista);
			}		
		});
	});
}