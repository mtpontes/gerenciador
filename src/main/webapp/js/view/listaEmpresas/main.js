import { clickEventPaginationtIndex } from "../../modules/pagination/paginationConfigs.js";
import { elementFactory } from "../listaEmpresas/elementFactory.js";
document.addEventListener('DOMContentLoaded', paginationEvent);


function paginationEvent(){
	//atualiza os elementos .lista
	let factory = elementFactory;
	clickEventPaginationtIndex(null, null, factory).addEventClick();
}
