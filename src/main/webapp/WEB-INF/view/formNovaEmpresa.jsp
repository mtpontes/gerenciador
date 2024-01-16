<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:url value="/empresa" var="linkEmpresaServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/view/formCadastroEmpresas.css">
	
	<script src="js/validation/validaCadastroEmpresa.js" type="module"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/modules/header.jsp"></jsp:include>
	<h2 class="titulo">CADASTRO</h2>
	
	<form class="formulario" id="formulario" action="${linkEmpresaServlet }" method="post">
		<div class="container-nome-e-data">
			<p class="texto">Nome</p>
			<p class="texto">Data Abertura</p> 
		</div>
			
		<div class="container-input">
			<input class="form-input" id="nome" name="nome" type="text"  placeholder="Nome da empresa" style="color: black"/>
			<input class="form-input" id="data" name="data" type="text"  placeholder="Exemplo: 01/01/2001" style="color: black"/>
		</div>
		
		<div class="container-erro">
			<div class="container-erro-div">
				<span class="form-label" id="nomeErro" style="display:none">Nome precisa ser preenchido</span>
			</div>
			<div class="container-erro-div">
				<span class="form-label" id="dataErro" style="display:none">Insira a data no formato dd/MM/aaaa</span>
			</div>
		</div>
	
		<input type="hidden" name="acao" value="novaEmpresa">
		<input class="botao-enviar" type="submit" value="Enviar" />
	</form>
	
	<footer></footer>
</body>
</html>