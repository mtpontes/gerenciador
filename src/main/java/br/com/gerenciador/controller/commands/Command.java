package br.com.gerenciador.controller.commands;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {

    public void executa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}