export const API_CONFIG = {
	EMPRESA: {
		URL_RELATIVA: '/gerenciador/empresa',
		
		PARAM_ACAO: {
			ATUALIZA: 'atualizaEmpresa',
			ARQUIVA: 'removeEmpresa',
			SEARCH: 'search',
			LISTA_EMPRESAS: 'listaEmpresas',
			LISTA_EMPRESAS_USUARIO: 'listaEmpresasUsuario',
			LISTA_EMPRESAS_DESATIVADAS_USUARIO: 'listaEmpresasDesativadasUsuario'
		}
	},
	
	getParamAcao: function(value){
		return '?acao=' + value;
	},
	
	getUrlRelativaComParamAcao: function(param){
		return this.EMPRESA.URL_RELATIVA + this.getParamAcao(param);
	}
}



