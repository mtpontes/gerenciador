import { putRequest } from "./ajax/ajax.js";
import { cssColors } from "./variaveisCSS.js";

//eventos
document.addEventListener("DOMContentLoaded", defineTipoLista);
document.addEventListener("DOMContentLoaded", mudaIcones);
document.addEventListener("DOMContentLoaded", mostraCadastrosDesativos)
document.addEventListener("DOMContentLoaded", removeRestauraAJAX);

//exibe oculta todas as empresas desativadas
function defineTipoLista() {
    console.log("defineTipoLista!");
    
    document.querySelectorAll('.lista').forEach(elemento => {
        const ativo = elemento.dataset.ativo;
        elemento.style.display = ativo === 'false' ? 'none' : 'flex';
    });
}

//muda o ícone dos botões de acordo com a propriedade dataset.ativo
//quando o botão 'Arquivados' é clicado, o ícone e texto de todos os botões 'Arquivar' são alterados para ícone e texto 'Desaquivar'
function mudaIcones() {
    console.log("mudaIcones!");
    const arquivados = document.getElementById('arquivados');
    //variável que guarda o estado do botão (ativado ou desativado)
    let estado = false;

    arquivados.addEventListener('click', function () {
        estado = !estado;
        const botaoArquiva = document.querySelectorAll('.container-arquiva');

        botaoArquiva.forEach(function (botao) {
            const valorDeAtivo = botao.dataset.ativo;
            const texto = botao.querySelector('.texto-arquiva');
            const icone = botao.querySelector('.icone-arquiva');

            //atualiza o texto e ícone com base no valor do atributo 'ativo'
            texto.innerText = valorDeAtivo === 'false' ? 'Desarquivar' : 'Arquivar';
            icone.innerText = valorDeAtivo === 'false' ? 'unarchive' : 'archive';
        });
        
        atualizaEstiloArquivados(estado);
    });
}

//muda o estilo do botao 'arquivados'
function atualizaEstiloArquivados(estado) {
    const arquivados = document.getElementById('arquivados');
    const colors = cssColors();
	
	//altera estilo do botao    
    arquivados.style.color = estado ? colors.corPrincipal : colors.corBranca;
    arquivados.style.backgroundColor = estado ? colors.corTerciaria : colors.corSecundaria;
    arquivados.style.transition = 'background-color 0.3s';

    //emula o comportamento da subclase hover pois ele se perde quando o estilo é manipulado
    arquivados.addEventListener('mouseover', function() {
        arquivados.style.backgroundColor = colors.corBranca;
        arquivados.style.color = estado ? colors.corSecundaria : colors.corPrincipal;
    });
    arquivados.addEventListener('mouseout', function() {
        arquivados.style.backgroundColor = estado ? colors.corTerciaria : colors.corSecundaria;
        arquivados.style.color = estado ? colors.corPrincipal : colors.corBranca;
    });
}

function mostraCadastrosDesativos() {
    console.log("mostraCadastrosDesativos rodou!");

    const botao = document.getElementById('arquivados');
    //quando o botão 'Arquivados' é clicado, alterna a exibição de todas as Empresas
    botao.addEventListener('click', function () {
        //captura elementos que representam uma linha, cada linha é uma Empresa
        let elementos = document.querySelectorAll('.lista');

        //itera sobre as linhas
        elementos.forEach(function (elemento) {
            //alterna entre exibição e ocultação da linha
            elemento.dataset.exibicao = (elemento.dataset.exibicao === 'false').toString();
            elemento.style.display = elemento.dataset.exibicao === 'true' ? 'flex' : 'none';
        });
    });
}

async function removeRestauraAJAX(){
	console.log("removeRestauraAJAX rodou!");
	
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
			const resposta =  await putRequest(url, campo);
			
			if(resposta.response == true){
				alteraTipoBotao(button, element);
				ocultaElemento(element);
			}
		})
	})
}

function alteraTipoBotao(button, element) {
    //altera para o valor oposto de 'ativo'
    const novoEstado = button.dataset.ativo === 'false' ? 'true' : 'false';
    
    //atualiza os atributos e a URL do botão
    button.href = button.href;
    button.dataset.ativo = novoEstado;
    element.dataset.ativo = novoEstado;
}

function ocultaElemento(element) {
    element.style.display = 'none';
    element.dataset.exibicao = 'false';
}
