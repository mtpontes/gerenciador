<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, br.com.alura.gerenciador.dto.empresa.EmpresaBaseDTO"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="java.time.LocalDate" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Catalogo de Empresas</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/modules/pagination.css">
	
	<script src="js/modules/pagination/eventosPagina.js" type="module"></script>
</head>
   	<div class="paginacao" data-currentPage="${currentPage}" data-totalPages="${totalPages}" data-pageSize="${pageSize}" id="paginacao">
		<a class="pagina anterior arrow index material-symbols-outlined" data-page="" data-size="${pageSize}" data-acao=""  href="acao=&size=&page=">chevron_left</a>

		<div class="colecao-paginas" id="colecao-paginas">
			<a class="start index" data-page="" data-size="${pageSize}" data-acao=""  href="acao=&size=&page=1">1</a>
			<p class="ellipse start">...</p>

			<a class="index-page index" data-page="" data-size="${pageSize}" data-acao="" href="acao=&size&page=">1</a>
			<a class="index-page index" data-page="" data-size="${pageSize}" data-acao="" href="acao=&size&page=">2</a>
			<a class="index-page index" data-page="" data-size="${pageSize}" data-acao="" href="acao=&size&page=">3</a>

    		<p class="ellipse end">...</p>
			<a class="end index" data-page="" data-size="${pageSize}" data-acao=""  href="acao=&size=&page=${totalPages }">${totalPages }</a>
		</div>
    		
		<a class="pagina seguinte arrow index material-symbols-outlined" data-page="" data-size="${pageSize}" data-acao=""  href="acao=&size=&page=">chevron_right</a>
    </div>
</html>
