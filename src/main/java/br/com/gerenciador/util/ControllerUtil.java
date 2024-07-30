package br.com.gerenciador.util;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ControllerUtil {

	public static String enderecoJSP(String nomeDoJSP) {
		return "WEB-INF/view/".concat(nomeDoJSP);
	}
	public static String usuarioParamAcao(String nomeDoMetodo) {
		return "usuario?acao=".concat(nomeDoMetodo);
	}
	public static String empresaParamAcao(String nomeDoMetodo) {
		return "empresa?acao=".concat(nomeDoMetodo);
	}
	
	public static JsonObject converteCorpoRequisicaoParaJsonObject(
		HttpServletRequest request
	) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        
        try {
        	//lê uma linha, quando terminar retorna null
        	while ((line = reader.readLine()) != null) {
        		stringBuilder.append(line);
        	}

        	String corpoDaRequisicaoEmString = stringBuilder.toString();
        	JsonObject jsonObject = JsonParser
				.parseString(corpoDaRequisicaoEmString)
				.getAsJsonObject();
        	return jsonObject;

		} catch (JsonParseException e) {
	        throw new IOException(
				"Erro ao analisar o corpo da requisição como JSON", e);
		}
	}
}