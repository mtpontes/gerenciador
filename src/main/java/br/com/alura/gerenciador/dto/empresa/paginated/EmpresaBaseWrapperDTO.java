package br.com.alura.gerenciador.dto.empresa.paginated;

import java.util.List;

import br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO;
import br.com.alura.gerenciador.pagination.Pagination;

public record EmpresaBaseWrapperDTO(List<EmpresaBaseDTO> empresas, Pagination pagination, String acao) {
	
	public List<EmpresaBaseDTO> getEmpresas(){
		return this.empresas;
	}
	public Pagination getPagination(){
		return this.pagination;
	}
}