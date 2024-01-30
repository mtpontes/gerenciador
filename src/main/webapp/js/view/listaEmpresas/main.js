import { clickEventPaginationtIndex } from "../../modules/pagination/paginationConfigs.js";
import { ListaEmpresasElementFactory } from "../listaEmpresas/ListaEmpresasElementFactory.js";
document.addEventListener('DOMContentLoaded', paginationEvent);

/**
 * Controla a lógica de paginação quando os índices de página são clicados.
 * - Utiliza a fábrica de elementos (elementFactory) para criar novos elementos.
 * - Adiciona eventos de clique aos índices de página e realiza a paginação.
 */
function paginationEvent(){
	//atualiza os elementos .lista
	let elementFactory = new ListaEmpresasElementFactory();
	const paginationEvent = clickEventPaginationtIndex(null, null, elementFactory);
	
	paginationEvent.addEventClick();
}
