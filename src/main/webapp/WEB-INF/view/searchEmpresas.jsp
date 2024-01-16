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
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/view/listaEmpresas.css">
	<link rel="stylesheet" href="styles/view/searchEmpresas.css">
	<script src="js/view/searchEmpresas/main.js" type="module"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/modules/header.jsp"></jsp:include>
	
	<c:set var="cardOff" value="${empty empresas ? 'card-off' : null}"></c:set>
	<section class="card ${cardOff}">
		<div class="container-titulo">
			<h1 class="titulo">Resultados da Pesquisa</h1>
		</div>
		
		<ul class="container-empresas container-off" id="containerEmpresas">
			<li class="colunas" id="colunas">
				<p class="coluna-nome">Nome</p>
				<p class="coluna-data">Abertura</p>
			</li>
		
		    <c:forEach items="${empresas}" var="empresa">
	        <li class="lista" id="lista">
	       		<p class="lista-nome">${empresa.nome }</p>
	       		<p class="lista-data">${empresa.data }</p>
	        </li>
		    </c:forEach>
		</ul>
	</section>
	
	<section class="listagemVazia" style="display:none">
    	<h1 class="listagemVazia-titulo">Nenhuma empresa foi encontrada com esse critério de pesquisa.</h1>
    	<h2 class="listagemVazia-subtitulo">Tente novamente com outro nome...</h2>
	</section>
    <jsp:include page="/WEB-INF/modules/paginacao.jsp"></jsp:include>
	<footer></footer>
</body>
</html>