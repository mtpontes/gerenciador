package br.com.gerenciador.dto.empresa.response;

import java.util.List;

import br.com.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.gerenciador.pagination.Pagination;

public record EmpresaBaseWrapperDTO(List<EmpresaBaseDTO> empresas, Pagination pagination) {
	
	public List<EmpresaBaseDTO> getEmpresas(){
		return this.empresas;
	}
	public Pagination getPagination(){
		return this.pagination;
	}
}
