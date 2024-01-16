package br.com.alura.gerenciador.pagination;

import lombok.Getter;

@Getter
public class PaginationBuilder {
	
    private Integer pageNumber = 1;
	private Integer pageSize = 5;
    private Integer startIndex;
    private Integer totalPages;

    //define o número da página a ser exibido
    public PaginationBuilder setPageNumber(String pageNumber) {
        try {
            Integer parsed = Integer.parseInt(pageNumber);
            this.pageNumber = Math.max(parsed, 1);
        } catch (NumberFormatException e) {
            this.pageNumber = 1;
        }
        return this;
    }
    //define o tamanho da página
    public PaginationBuilder setPageSize(String pageSize) {
        try {
            Integer parsed = Integer.parseInt(pageSize);
            parsed = Math.max(parsed, 3);
            this.pageSize = Math.min(parsed, 20);
        } catch (NumberFormatException e) {
            this.pageSize = 5;
        }
        return this;
    }
    //calcula qual será o indice inicial da consulta no DB
    private void setStartIndex() {
        this.startIndex = (this.pageNumber - 1) * this.pageSize;
    }
    //define o número total de páginas com base no número total de registros e no tamanho da página
    public PaginationBuilder setTotalPages(Long totalPages) {
        this.totalPages = Math.max((int) Math.ceil((double) totalPages / this.pageSize), 1);
        return this;
    }

    public Pagination build() {
    	//garante que pageNumber não seja maior que totalPages
    	this.pageNumber = (this.pageNumber > this.totalPages) ? this.totalPages : this.pageNumber;
        setStartIndex();
        return new Pagination(this);
    }
}