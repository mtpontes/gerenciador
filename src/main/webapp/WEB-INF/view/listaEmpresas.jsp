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
	<link rel="stylesheet" href="styles/listaEmpresas.css">
</head>
<body>
	<!-- inclui header.jsp -->
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="container-titulo">
		<h1 class="titulo">Outras Empresas</h1>
	</div>
	
	<ul class="container-empresas">

		<li class="colunas">
			<div class="colunas-container">
				<p class="coluna-nome">Nome</p>
				<p class="coluna-data-lista-empresas">Data</p>
			</div>
		</li>
	
	    <c:forEach items="${empresas}" var="empresa">
	        <li class="lista">
           		<p class="lista-nome">${empresa.nome }</p>
           		<p class="lista-data">${empresa.data }</p>
	        </li>
	    </c:forEach>
	</ul>

	<footer></footer>
</body>
</html>