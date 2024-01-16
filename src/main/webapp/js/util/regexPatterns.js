export const usuarioRegexPatterns = {
	//Usuario patterns
	NOME_USUARIO_PATTERN: new RegExp(/^[A-Za-zÀ-ÖØ-öø-ÿ' _-]+$/),
	LOGIN_USUARIO_PATTERN: new RegExp(/^.{3,20}$/),
	SENHA_USUARIO_PATTERN: new RegExp(/.{8,}/),
	CONFIRMA_SENHA_USUARIO_PATTERN: new RegExp(/.+/)
}

export const empresaRegexPatterns = {
	//Empresa patters
	NOME_EMPRESA_PATTERN: new RegExp(/^[a-zA-Z0-9\s.'-]{1,100}$/),
	DATA_EMPRESA_PATTERN: new RegExp(/^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/),
}

export const urlRelativaEmpresa = '/gerenciador/empresa'
export const urlRelativaUsuario = '/gerenciador/usuario'