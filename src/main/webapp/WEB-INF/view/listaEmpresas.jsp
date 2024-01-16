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
	
	<script src="js/view/listaEmpresas/main.js" type="module"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/modules/header.jsp"></jsp:include>
	<div class="container-titulo">
		<h1 class="titulo">Outras Empresas</h1>
	</div>
	
	<ul class="container-empresas" id="containerEmpresas">
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
	
    <jsp:include page="/WEB-INF/modules/paginacao.jsp"></jsp:include>
	<footer></footer>
</body>
</html>