package br.com.alura.gerenciador.dto.empresa.response.paginated;

import java.util.List;

import br.com.alura.gerenciador.dto.empresa.response.ListaEmpresasUsuarioDTO;
import br.com.alura.gerenciador.pagination.Pagination;

public record ListaEmpresasUsuarioWrapperDTO(List<ListaEmpresasUsuarioDTO> empresas, Pagination pagination) {
	
	public List<ListaEmpresasUsuarioDTO> getEmpresas(){
		return this.empresas;
	}
	public Pagination getPagination(){
		return this.pagination;
	}
}
