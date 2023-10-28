<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:url value="/entrada" var="linkEntradaServlet"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Insert title here</title>
	<link rel="stylesheet" href="styles/reset.css">
	<link rel="stylesheet" href="styles/formAlteraEmpresa.css">
</head>
<body>

	<jsp:include page="header.jsp"></jsp:include>
	<h2 class="titulo">CADASTRO</h2>
	
	<form class="formulario" action="${linkEntradaServlet }" method="post">
	
		<div class="container">
			<div class="container-nome-e-data">
				<p class="container-nome-e-data-texto">Nome</p>
				<p class="container-nome-e-data-texto">Data Abertura</p> 
			</div>
			
			<div class="container-campo">
				<input class="container-campo-input" type="text" name="nome" placeholder="Nome da empresa" style="color: black"/>
				<input class="container-campo-input" type="text" name="data" placeholder="Exemplo: 01/01/2001" pattern="\d{2}/\d{2}/\d{4}" style="color: black"/>
			</div>
		</div>
	
		<input type="hidden" name="acao" value="NovaEmpresa">
	
		<div class="container-enviar">
			<input class="formulario-container-enviar" type="submit" value="Enviar" />
		</div>
	</form>
	
	</body>
	</html>