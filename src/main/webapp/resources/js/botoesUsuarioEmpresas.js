import { postRequest } from "./ajax.js";
//eventos
document.addEventListener("DOMContentLoaded", defineTipoLista);
document.addEventListener("DOMContentLoaded", mudaIcones);
document.addEventListener("DOMContentLoaded", mostraCadastrosDesativos)
document.addEventListener("DOMContentLoaded", removeRestauraAJAX);

//exibe oculta todas as empresas desativadas
function defineTipoLista() {
	const elementos = document.querySelectorAll('.lista');

	elementos.forEach(function(elemento) {
		var ativo = elemento.dataset.ativo;

		if (ativo == 'false') {
			elemento.style.display = 'none';
		}
	});
}

//quando o botão 'Arquivados' é clicado, o ícone e texto de todos os botões 'Arquivar' são alterados para ícone e texto 'Desaquivar'
function mudaIcones(){
	const arquivados = document.getElementById('arquivados');
	let estado = false;
	
	//adiciona um evento de click ao botão 'Arquivados'
	arquivados.addEventListener('click', function(){
		const botaoArquiva = document.querySelectorAll('.container-arquiva');
		
		estado = !estado;

		botaoArquiva.forEach(function(botao){
			//captura o elemento que representa o texto 'Arquivar'
			const texto = botao.querySelector('.texto-arquiva')
			//captura o elemento que representa o ícone
			const icone = botao.querySelector('.icone-arquiva');
			const valorDeAtivo = botao.dataset.ativo;
			
			//se esta for uma Empresa desativada (ativo == false)
			if(valorDeAtivo == 'false'){
				//muda os ícones para interface interface dos arquivados
				texto.innerText = 'Desarquivar';
				icone.innerText = 'unarchive';
			} else {
				//se for uma Empresa ativa, exibe botão para arquivar (desativar)
				texto.innerText = 'Arquivar';
				icone.innerText = 'archive';
			}
		});
		
		//muda o background do botão 'Arquivados' quando a exibição dos itens arquivados está ativa
        if (estado) {
			arquivados.style.color = '#1A120B'
            arquivados.style.backgroundColor = '#D5CEA3';
        } else {
			arquivados.style.color = '#F4F4F4F4';
            arquivados.style.background = 'none';
        }
	})
}


function mostraCadastrosDesativos(){
		//captura o botão 'Arquivados'
		const botao = document.getElementById('arquivados');
		
		//quando o botão 'Arquivados' é clicado, oculta todas as Empresas ativadas e exibe todas as desativadas
		botao.addEventListener('click', function(){
			//captura elementos que representam uma linha, cada linha é uma Empresa
			let elementos = document.querySelectorAll('.lista');
		
			//itera sobre as linhas
			elementos.forEach( function(elemento){
				
				if(elemento.dataset.e == 'false'){
					
					//essa propriedade é usada para verificar se o elemento está sendo exibido ou não
					elemento.dataset.e = 'true';
					//exibe a linha
					elemento.style.display = 'flex';
				} else if (elemento.dataset.e == 'true') {
					
					//essa propriedade é usada para verificar se o elemento está sendo exibido ou não
					elemento.dataset.e = 'false';
					//oculta a linha
					elemento.style.display = 'none';
					}
				})
			})
}


async function removeRestauraAJAX(){
	//captura todos os botões 'Arquivar'
	var removeButtons = document.querySelectorAll('.container-arquiva');

	//adiciona evento de click a todos os botões 'Arquivar'	
	removeButtons.forEach( async function(button){
		button.addEventListener('click', async function(event){
			event.preventDefault();
			
			//pega a URL atual (o botão é um link)
			var url = this.href;
			
			//pega o parametro 'id' da URL
            var idEmpresa = new URLSearchParams(new URL(url).search).get('id');
            //pega o <li> que imprime esse elemento
            let element = document.querySelector('.class-'.concat(idEmpresa));
            //cria o corpo de uma requisição
			let campo = {
				acao: 'removeEmpresa',
				id: idEmpresa
			};
			
			//requisição AJAX
			const resposta =  await postRequest(url, campo);
			
			if(resposta.response == true){
				alteraTipoBotao(button, element);
			}
		})
	})
}

//
async function alteraTipoBotao(button, element) {
	
	//se a empresa estava desativada, altere para ativada e oculte da lista de exibição atual
	if (button.dataset.ativo == 'false') {
		button.href = button.href;
		
		button.dataset.ativo = 'true'
		element.dataset.ativo = 'true'
		
		//oculta o elemento
		element.style.display = 'none'
		//essa propriedade é usada para verificar se o elemento está sendo exibido ou não
		element.dataset.e = 'false'
	
	//se a empresa estava ativada, altere para desativada e oculte da lista de exibição atual
	} else if (button.dataset.ativo == 'true') {
		button.href = button.href;
		
		button.dataset.ativo = 'false';
		element.dataset.ativo = 'false';
		
		//oculta o elemento
		element.style.display = 'none';
		//essa propriedade é usada para verificar se o elemento está sendo exibido ou não
		element.dataset.e = 'false';
	}
}