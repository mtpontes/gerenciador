package main.java.br.com.alura.gerenciador.acao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AcaoSemEntityManager {
    String executa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
