package com.mycompany.consultorio.medico.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/resources/*"})
public class AuthFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI();
        
        // Permitir acceso sin autenticación a login y logout
        if (path.endsWith("/auth/login") || path.endsWith("/auth/logout") || path.endsWith("/auth/session")) {
            chain.doFilter(request, response);
            return;
        }
        
        // VALIDACIÓN DESACTIVADA PARA TESTING
        /*
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"No autorizado. Debe iniciar sesión.\"}");
            return;
        }
        */
        
        // Continuar con la petición
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void destroy() {
    }
}
