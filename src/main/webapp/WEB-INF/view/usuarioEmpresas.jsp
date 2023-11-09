<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List,main.java.br.com.alura.gerenciador.modelo.Empresa"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="main.java.br.com.alura.gerenciador.util.DateUtil" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Minhas Empresas</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/listaEmpresasUsuario.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
	<script src="resources/js/botoesUsuarioEmpresas.js" type="module"></script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="container-titulo">
		<h1 class="titulo">Minhas Empresas</h1>
		
		<a class="add material-symbols-outlined" href="empresa?acao=novaEmpresaForm" method='get'>add</a>
	</div>
	
	<ul class="container-empresas">
		
		<li class="colunas">
			<div class="coluna-nome-data">
				<p class="coluna-nome">Nome</p>
				<p class="coluna-data" >Data</p>
			</div>
			
			<p class="void"></p>
			
			<button class="arquivados" id="arquivados">
				<span class="texto-arquivados" id="texto-arquivados">Arquivados</span>
				<span class="icone-arquivados material-symbols-outlined" id="icone-arquivados">archive</span>
			</button>
		</li>
	
		<c:forEach items="${empresas}" var="empresa">
			<li class="lista class-${empresa.id }" data-ativo=${empresa.ativo } data-e=${empresa.ativo }>
				<div class="lista-nome-data">
					<p class="lista-nome">${empresa.nome }</p>
	        		<p class="lista-data">${DateUtil.formatDate(empresa.dataAbertura, "dd/MM/yyyy")}</p>
				</div>
				
				
	        	<div class="container-edita-arquiva">
	        		<a class="editar" id="edita" href="/gerenciador/empresa?acao=mostraEmpresa&nome=${empresa.nome }&id=${empresa.id}" method='get'>
	        			<span class="texto-editar">Editar</span>
	        			<span class="icone-editar material-symbols-outlined">edit</span>
	        		</a>
	        		
	        		
					<a class="container-arquiva" id="container-arquiva" href="/gerenciador/ajax?acao=removeEmpresa&id=${empresa.id }" method='post' data-ativo=${empresa.ativo }>
						<span class="texto-arquiva">Arquivar</span>
						<span class="icone-arquiva material-symbols-outlined">archive</span>
					</a>
				</div>
			</li>
		</c:forEach>
	</ul>
	
	<footer></footer>

</body>
</html>
