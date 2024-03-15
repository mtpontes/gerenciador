package br.com.gerenciador.dto.empresa.response;

import java.util.List;

import br.com.gerenciador.pagination.Pagination;

public record ListaEmpresasUsuarioWrapperDTO(List<ListaEmpresasUsuarioDTO> empresas, Pagination pagination) {
	
	public List<ListaEmpresasUsuarioDTO> getEmpresas(){
		return this.empresas;
	}
	public Pagination getPagination(){
		return this.pagination;
	}
}
