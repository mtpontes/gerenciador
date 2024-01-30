import { ElementFactory } from "../../modules/elementFactory/ElementFactory";

/**
 * Classe que cria um novo elemento HTML representando uma empresa.
 *
 * @param {Object} empresa - Objeto contendo informações sobre a empresa.
 */
export class ListaEmpresasElementFactory extends ElementFactory {
	createElement(empresa) {
	    const novoLi = document.createElement('li');
	    novoLi.classList.add('lista');
	    
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
	    
	    return novoLi;
    }
}
