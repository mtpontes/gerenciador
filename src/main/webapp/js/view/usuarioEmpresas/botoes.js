import { cssColors } from "../../util/variaveisCSS.js";

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