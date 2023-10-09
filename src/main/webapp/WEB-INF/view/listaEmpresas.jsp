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
	
	<div class="titulo-lista">
		<h1 class="titulo-lista-texto">Lista de empresas</h1>
	</div>
	
	<ul class="container-empresas">

		<li class="colunas">
			<div class="colunas-container">
				<div class="colunas-container-nome">
					<p class="colunas-container-nome-texto">Nome</p>
				</div>
				<div class="colunas-container-data" style="padding: 0 2.5em 0 0;">
					<p class="colunas-container-data-texto">Data</p>
				</div>
			</div>
		</li>
	
	    <c:forEach items="${empresas}" var="empresa">
	        <li class="lista">
	        	<div class="lista-container">
	        		<div class="lista-container-nome">
	            		<p class="lista-container-nome-texto nome">${empresa.nome }</p>
	            	</div>
	            
	            	<div class="lista-container-data">
	            		<p class="lista-container-data-texto data">${DateUtil.formatDate(empresa.dataAbertura, "dd/MM/yyyy")}</p>
	           		</div>
	            </div>
	        </li>
	    </c:forEach>
	</ul>

	<footer class="footer"></footer>
</body>
</html>











