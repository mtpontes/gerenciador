package br.com.gerenciador.controller.commands.usuario;

import java.io.IOException;

import br.com.gerenciador.controller.commands.Command;
import br.com.gerenciador.util.ControllerUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutCommand implements Command {

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response
    ) throws IOException, ServletException {
        HttpSession sessao = request.getSession();
        sessao.invalidate();
        response.sendRedirect(
            ControllerUtil.usuarioParamAcao("loginForm"));
    }
}