/**
 * Constante que contém configurações relacionadas à API, especialmente para a entidade EMPRESA.
 * 
 * @constant {Object} API_CONFIG
 * @property {Object} EMPRESA - Configurações específicas para a entidade EMPRESA.
 * @property {string} EMPRESA.URL_RELATIVA - URL relativa para a entidade EMPRESA.
 * @property {Object} EMPRESA.PARAM_ACAO - Parâmetros de ação para a entidade EMPRESA.
 * @property {string} EMPRESA.PARAM_ACAO.ATUALIZA - Parâmetro de ação para atualizar empresa.
 * @property {string} EMPRESA.PARAM_ACAO.ARQUIVA - Parâmetro de ação para arquivar empresa.
 * @property {string} EMPRESA.PARAM_ACAO.SEARCH - Parâmetro de ação para pesquisa.
 * @property {string} EMPRESA.PARAM_ACAO.LISTA_EMPRESAS - Parâmetro de ação para listar empresas.
 * @property {string} EMPRESA.PARAM_ACAO.LISTA_EMPRESAS_USUARIO - Parâmetro de ação para listar empresas do usuário. Por padrão ele retorn as empresas com ativo === true, mas também aceita o parâmetro ativo === false pela URL
 * @property {function} getParamAcao - Função que retorna um parâmetro de ação formatado com base em um valor.
 * @property {function} getUrlRelativaComParamAcao - Função que retorna uma URL relativa com um parâmetro de ação.
 */
export const API_CONFIG = {
	EMPRESA: {
		URL_RELATIVA: '/gerenciador/empresa',
		
		PARAM_ACAO: {
			ATUALIZA: 'atualizaEmpresa',
			ARQUIVA: 'removeEmpresa',
			SEARCH: 'searchAjax',
			LISTA_EMPRESAS: 'listaEmpresasAjax',
			LISTA_EMPRESAS_USUARIO: 'listaEmpresasAtivoUsuarioAjax',
		}
	},
	
	getParamAcao: function(value){
		return '?acao=' + value;
	},
	
	getUrlRelativaComParamAcao: function(param){
		return this.EMPRESA.URL_RELATIVA + this.getParamAcao(param);
	}
}



