import { ElementFactory } from "../../modules/elementFactory/ElementFactory.js"

/**
 * Classe que cria um novo elemento HTML representando uma empresa.
 *
 * @param {Object} empresa - Objeto contendo informações sobre a empresa.
 */
export class UsuarioEmpresasElementFactory extends ElementFactory {
	createElement(empresa) {
		//form .lista
	    const novoForm = document.createElement('form');
	    novoForm.classList.add('lista');
	    novoForm.id = empresa.id;
	    novoForm.dataset.ativo = empresa.ativo;
	    
		//div .lista-nome-data
	    const divNomeData = document.createElement('div');
	    divNomeData.classList.add('lista-nome-data');
		//p .lista-nome
	    const paragrafoNome = document.createElement('p');
	    paragrafoNome.classList.add('lista-nome');
	    paragrafoNome.id = 'lista-nome';
	    paragrafoNome.textContent = empresa.base.nome;
		//p .lista-data
	    const paragrafoData = document.createElement('p');
	    paragrafoData.classList.add('lista-data');
	    paragrafoData.id = 'lista-data';
	    paragrafoData.textContent = empresa.base.data;
		//input .entrada-nome
	    const inputNome = document.createElement('input');
	    inputNome.classList.add('entrada-nome', 'lista-nome');
	    inputNome.style.display = 'none';
		//input .entrada-data
	    const inputData = document.createElement('input');
	    inputData.classList.add('entrada-data', 'lista-data');
	    inputData.style.display = 'none';
	    
	
		//div .container-edita-arquiva
	    const divEditaArquiva = document.createElement('div');
	    divEditaArquiva.classList.add('container-edita-arquiva');
	    
		//a .botao-editar
	    const linkEditar = document.createElement('a');
	    linkEditar.classList.add('botao-editar');
	    linkEditar.id = 'botao-editar';
	    linkEditar.href = `/gerenciador/empresa?acao=mostraEmpresa&nome=${empresa.base.nome}&id=${empresa.id}&data=${empresa.base.data}`;
		//span .texto-editar
	    const spanTextoEditar = document.createElement('span');
	    spanTextoEditar.classList.add('texto-editar');
	    spanTextoEditar.textContent = 'Editar';
		//span .icone-editar
	    const spanIconeEditar = document.createElement('span');
	    spanIconeEditar.classList.add('icone-editar', 'material-symbols-outlined');
	    spanIconeEditar.textContent = 'edit';
	
	    linkEditar.appendChild(spanTextoEditar);
	    linkEditar.appendChild(spanIconeEditar);
		
		//a .botao-arquivar	
	    const linkArquivar = document.createElement('a');
	    linkArquivar.classList.add('botao-arquivar');
	    linkArquivar.id = 'botao-arquivar';
	    linkArquivar.dataset.empresaid = empresa.id;
	    linkArquivar.href = `/gerenciador/empresa?acao=removeEmpresa`;
		//span .texto-arquiva
	    const spanTextoArquiva = document.createElement('span');
	    spanTextoArquiva.classList.add('texto-arquiva');
	    spanTextoArquiva.textContent = (empresa.ativo == false) ? 'Desarquivar' : 'Arquivar';
		//span .icone-arquiva
	    const spanIconeArquiva = document.createElement('span');
	    spanIconeArquiva.classList.add('icone-arquiva', 'material-symbols-outlined');
	    const valor = (empresa.ativo == false) ? 'unarchive' : 'archive';
	    spanIconeArquiva.textContent = valor;
	
	    linkArquivar.appendChild(spanTextoArquiva);
	    linkArquivar.appendChild(spanIconeArquiva);
	    
	
		//input .botao-enviar
	    const botaoEnviar = document.createElement('input');
	    botaoEnviar.classList.add('botao-enviar');
	    botaoEnviar.id = 'inputHidden';
	    botaoEnviar.type = 'submit';
	    botaoEnviar.value = 'Enviar';
	    botaoEnviar.style.display = 'none';
	
	    divNomeData.appendChild(paragrafoNome);
	    divNomeData.appendChild(paragrafoData);
	    divNomeData.appendChild(inputNome);
	    divNomeData.appendChild(inputData);
	
	    divEditaArquiva.appendChild(linkEditar);
	    divEditaArquiva.appendChild(linkArquivar);
	
	    novoForm.appendChild(divNomeData);
	    novoForm.appendChild(divEditaArquiva);
	    novoForm.appendChild(botaoEnviar);
		
		return novoForm;	
	}
}
