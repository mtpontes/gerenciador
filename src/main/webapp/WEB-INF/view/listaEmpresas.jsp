<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List,main.java.br.com.alura.gerenciador.modelo.Empresa"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="main.java.br.com.alura.gerenciador.util.DateUtil" %>


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
           		<p class="lista-data">${DateUtil.formatDate(empresa.dataAbertura, "dd/MM/yyyy")}</p>
	        </li>
	    </c:forEach>
	</ul>

	<footer class="footer"></footer>
</body>
</html>











