package br.com.gerenciador.util;

import br.com.gerenciador.pagination.Pagination;
import br.com.gerenciador.pagination.PaginationBuilder;
import jakarta.servlet.http.HttpServletRequest;

public class PaginationUtil {

    public static Pagination criaPagination(
		HttpServletRequest request, 
		Long totalPages
	) {
		String paramPageNumber = request.getParameter("page");
		String paramPageSize = request.getParameter("size");
		Pagination pg = new PaginationBuilder()
			.setPageNumber(paramPageNumber)
			.setPageSize(paramPageSize)
			.setTotalPages(totalPages)
			.build();
		return pg;
	}
}