package br.com.gerenciador.controller;

import java.io.IOException;

import br.com.gerenciador.controller.commands.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotFoundCommand implements Command {

    @Override
    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}