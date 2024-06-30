package br.com.gerenciador.util;

import java.util.HashMap;
import java.util.Map;

import br.com.gerenciador.exception.DatabaseAccessException;
import br.com.gerenciador.exception.FormValidationException;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletResponse;

public class HttpStatusErrorMapperUtil {
    private static final Map<Class<? extends Exception>, Integer> errorMap = new HashMap<>();

    static {
    	errorMap.put(DatabaseAccessException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	errorMap.put(NoResultException.class, HttpServletResponse.SC_NOT_FOUND);
        errorMap.put(IllegalStateException.class, HttpServletResponse.SC_FORBIDDEN);
        errorMap.put(FormValidationException.class, HttpServletResponse.SC_BAD_REQUEST);
    }

    public static Integer getStatusCodeByException(Exception error) {
    	Class<? extends Exception> errorClass = error.getClass();
    	
        return errorMap.getOrDefault(errorClass, HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
    }
}