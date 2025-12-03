package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.UsuarioDAO;
import com.mycompany.consultorio.medico.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @POST
    @Path("login")
    public Response login(LoginRequest request, @Context HttpServletRequest httpRequest) {
        try {
            // Buscar usuario por nombre de usuario
            var usuarioOpt = usuarioDAO.findByNombreUsuario(request.nombreUsuario);
            
            if (usuarioOpt.isEmpty()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Usuario o contraseña incorrectos\"}")
                        .build();
            }
            
            Usuario usuario = usuarioOpt.get();
            
            // Verificar contraseña
            try {
                if (!BCrypt.checkpw(request.contrasena, usuario.getContrasenaHash())) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"error\": \"Usuario o contraseña incorrectos\"}")
                            .build();
                }
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"" + e.getMessage() + "\"}")
                        .build();
            }
            
            // Crear sesión
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("usuarioId", usuario.getIdUsuario());
            session.setAttribute("nombreUsuario", usuario.getNombreUsuario());
            session.setAttribute("rol", usuario.getRol().getNombreRol());
            session.setMaxInactiveInterval(3600); // 1 hora
            
            // Retornar datos del usuario
            return Response.ok(new LoginResponse(
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getRol().getNombreRol(),
                    usuario.getEmail()
            )).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    @Path("logout")
    public Response logout(@Context HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return Response.ok("{\"mensaje\": \"Sesión cerrada exitosamente\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("session")
    public Response getSession(@Context HttpServletRequest httpRequest) {
        try {
            HttpSession session = httpRequest.getSession(false);
            if (session == null || session.getAttribute("usuarioId") == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"No hay sesión activa\"}")
                        .build();
            }
            
            return Response.ok(new LoginResponse(
                    (Integer) session.getAttribute("usuarioId"),
                    (String) session.getAttribute("nombreUsuario"),
                    (String) session.getAttribute("rol"),
                    null
            )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    // Clases internas para requests/responses
    public static class LoginRequest {
        public String nombreUsuario;
        public String contrasena;
    }
    
    public static class LoginResponse {
        public Integer idUsuario;
        public String nombreUsuario;
        public String rol;
        public String email;
        
        public LoginResponse(Integer idUsuario, String nombreUsuario, String rol, String email) {
            this.idUsuario = idUsuario;
            this.nombreUsuario = nombreUsuario;
            this.rol = rol;
            this.email = email;
        }
    }
}
