import { cssColors } from "../../util/variaveisCSS.js";
import { putRequest } from "../../util/ajax.js";

function getState(){
	const acao = new URL(window.location.href).searchParams.get('acao');
	return (acao === 'listaEmpresasDesativadasUsuario') ? true : false;
}
//muda o estilo do botao 'arquivados'
export function atualizaEstiloArquivados() {
	const state = getState();
    const colors = cssColors();
    const arquivados = document.getElementById('arquivados');

	//altera estilo do botao    
	arquivados.style.color = state ? colors.corPrincipal : colors.corBranca;
	arquivados.style.backgroundColor = state ? colors.corTerciaria : 'none';

    arquivados.style.transition = 'background-color 0.3s';

    //emula o comportamento da subclase hover pois ele se perde quando o estilo é manipulado
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

export function eventArchiveUnarquive(collection){
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