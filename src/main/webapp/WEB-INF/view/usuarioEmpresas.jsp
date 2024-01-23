<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, br.com.alura.gerenciador.dto.empresa.ListaEmpresasUsuarioDTO"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:url value="/empresa" var="linkEmpresaServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Minhas Empresas</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/view/listaEmpresasUsuario.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
	
	<script src="js/view/usuarioEmpresas/botoes.js" type="module"></script>
	<script src="js/view/usuarioEmpresas/editarEmpresa.js" type="module"></script>
	<script src="js/view/usuarioEmpresas/main.js" type="module"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/modules/header.jsp"></jsp:include>
	<div class="container-titulo">
		<h1 class="titulo">Minhas Empresas</h1>
		<a class="add material-symbols-outlined" href="empresa?acao=novaEmpresaForm" method='get'>add</a>
	</div>
	
	<ul class="container-empresas" id="container-empresas">
		<li class="colunas-texto colunas">
			<div class="coluna-nome-data">
				<p class="coluna-nome">Nome</p>
				<p class="coluna-data">Abertura</p>
			</div>
			
			<div class="coluna-botao">
				<button class="arquivados" id="arquivados">
					<span class="texto-arquivados" id="texto-arquivados">Arquivados</span>
					<span class="icone-arquivados material-symbols-outlined" id="icone-arquivados">archive</span>
				</button>
			</div>
		</li>
	
		<c:forEach items="${empresas}" var="empresa">
		<form class="lista" id="${empresa.id}" data-empresaId="${empresa.id}" data-ativo="${empresa.ativo}">
			<div class="lista-nome-data">
				<p class="lista-nome" id="lista-nome">${empresa.nome}</p>
        		<p class="lista-data" id="lista-data">${empresa.data}</p>
        		
        		<input class="entrada-nome lista-nome" style="display:none">
        		<input class="entrada-data lista-data" style="display:none">
			</div>
			
        	<div class="container-edita-arquiva">
        		<a class="botao-editar" id="botao-editar" href="/gerenciador/empresa?acao=mostraEmpresa&nome=${empresa.nome}&id=${empresa.id}&data=${empresa.data}">
        			<span class="texto-editar">Editar</span>
        			<span class="icone-editar material-symbols-outlined">edit</span>
        		</a>
        		
				<a class="botao-arquivar" id="botao-arquivar" data-empresaid="${empresa.id}" href="/gerenciador/empresa?acao=removeEmpresa" method='put'>
					<span class="texto-arquiva">Arquivar</span>
					<span class="icone-arquiva material-symbols-outlined">archive</span>
				</a>
			</div>
			<input class="botao-enviar" id="inputHidden" type="submit" value="Enviar" style="display:none"/>
		</form>
		</c:forEach>
	</ul>
	
	<jsp:include page="/WEB-INF/modules/paginacao.jsp"></jsp:include>
	<footer></footer>
</body>
</html>
