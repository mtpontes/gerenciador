export function criaColecaoNovosElementosLi(empresas) {
	const colecaoNovosLi  = [];
	empresas.forEach(empresa => {
	    const novoFormulario = document.createElement('form');
	    novoFormulario.classList.add('lista');
	    novoFormulario.id = empresa.id;
	
	    const divNomeData = document.createElement('div');
	    divNomeData.classList.add('lista-nome-data');
	
	    const paragrafoNome = document.createElement('p');
	    paragrafoNome.classList.add('lista-nome');
	    paragrafoNome.id = 'lista-nome';
	    paragrafoNome.textContent = empresa.base.nome;
	
	    const paragrafoData = document.createElement('p');
	    paragrafoData.classList.add('lista-data');
	    paragrafoData.id = 'lista-data';
	    paragrafoData.textContent = empresa.base.data;
	
	    const inputNome = document.createElement('input');
	    inputNome.classList.add('entrada-nome', 'lista-nome');
	    inputNome.style.display = 'none';
	
	    const inputData = document.createElement('input');
	    inputData.classList.add('entrada-data', 'lista-data');
	    inputData.style.display = 'none';
	
	    const divEditaArquiva = document.createElement('div');
	    divEditaArquiva.classList.add('container-edita-arquiva');
	
	    const linkEditar = document.createElement('a');
	    linkEditar.classList.add('botao-editar');
	    linkEditar.id = 'botao-editar';
	    linkEditar.href = `/gerenciador/empresa?acao=mostraEmpresa&nome=${empresa.base.nome}&id=${empresa.id}&data=${empresa.base.data}`;
	
	    const spanTextoEditar = document.createElement('span');
	    spanTextoEditar.classList.add('texto-editar');
	    spanTextoEditar.textContent = 'Editar';
	
	    const spanIconeEditar = document.createElement('span');
	    spanIconeEditar.classList.add('icone-editar', 'material-symbols-outlined');
	    spanIconeEditar.textContent = 'edit';
	
	    linkEditar.appendChild(spanTextoEditar);
	    linkEditar.appendChild(spanIconeEditar);
	
	    const linkArquivar = document.createElement('a');
	    linkArquivar.classList.add('botao-arquivar');
	    linkArquivar.id = 'botao-arquivar';
	    linkArquivar.dataset.empresaid = empresa.id;
	    linkArquivar.href = `/gerenciador/empresa?acao=removeEmpresa`;
	
	    const spanTextoArquiva = document.createElement('span');
	    spanTextoArquiva.classList.add('texto-arquiva');
	    spanTextoArquiva.textContent = 'Arquivar';
	
	    const spanIconeArquiva = document.createElement('span');
	    spanIconeArquiva.classList.add('icone-arquiva', 'material-symbols-outlined');
	    spanIconeArquiva.textContent = 'archive';
	
	    linkArquivar.appendChild(spanTextoArquiva);
	    linkArquivar.appendChild(spanIconeArquiva);
	
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
	
	    novoFormulario.appendChild(divNomeData);
	    novoFormulario.appendChild(divEditaArquiva);
	    novoFormulario.appendChild(botaoEnviar);
	
		colecaoNovosLi.push(novoFormulario);
	});
	return colecaoNovosLi;	
}
