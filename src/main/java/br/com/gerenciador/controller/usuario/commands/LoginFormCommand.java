package br.com.gerenciador.controller.usuario.commands;

import java.io.IOException;

import br.com.gerenciador.controller.Command;
import br.com.gerenciador.util.ControllerUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFormCommand implements Command {
	
    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(ControllerUtil.enderecoJSP("formLogin.jsp"));
		rd.forward(request, response);
    }
}