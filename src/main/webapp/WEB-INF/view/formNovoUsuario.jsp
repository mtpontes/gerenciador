<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url value="/usuario" var="linkUsuarioServlet"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<c:import url="logout-parcial.jsp" />


	<form action="${linkUsuarioServlet }" method="post">
	
		Login: <input type="text" name="login"  />
		Senha: <input type="text" name="senha" pattern=".{8,}" title="A senha deve ter pelo menos 8 caracteres."/>
		
		<input type="hidden" name="acao" value="NovoUsuario">
	
		<input type="submit" />
	</form>

</body>
</html>