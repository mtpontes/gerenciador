package br.com.gerenciador.pagination;

import lombok.Getter;

@Getter
public class Pagination {
	private final Integer pageNumber;
	private final Integer pageSize;
	private final Integer startIndex;
	private final Integer totalPages;
	
	public Pagination(PaginationBuilder builder) {
		this.pageNumber = builder.getPageNumber();
		this.pageSize = builder.getPageSize();
		this.startIndex= builder.getStartIndex();
		this.totalPages = builder.getTotalPages();
	}
}
