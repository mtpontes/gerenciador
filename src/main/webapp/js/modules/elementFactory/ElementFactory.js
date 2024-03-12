/**
 * Classe abstrata que define um contrato para fábricas de elementos HTML.
 * As classes concretas que herdam dessa classe devem implementar o método
 * `createElement`, responsável por criar e retornar um novo elemento HTML
 * com base nos dados fornecidos.
 *
 * @abstract
 * @class ElementFactory
 * 
 * @param {Object} elementDataJson - Dados a serem utilizados na criação do elemento.
 */
export class ElementFactory {
	constructor(acao) {
		this.acao = acao;
	}
	
	createElement(elementDataJson){
	}
}