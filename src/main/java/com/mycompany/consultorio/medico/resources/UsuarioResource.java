package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.UsuarioDAO;
import com.mycompany.consultorio.medico.model.Usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

/**
 * Servicio REST para gestión de usuarios
 */
@Path("usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @GET
    public Response getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.findAll();
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getUsuarioById(@PathParam("id") Integer id) {
        try {
            return usuarioDAO.findById(id)
                    .map(usuario -> Response.ok(usuario).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Usuario no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("username/{username}")
    public Response getUsuarioByUsername(@PathParam("username") String username) {
        try {
            return usuarioDAO.findByNombreUsuario(username)
                    .map(usuario -> Response.ok(usuario).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Usuario no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("rol/{rol}")
    public Response getUsuariosByRol(@PathParam("rol") String rol) {
        try {
            List<Usuario> usuarios = usuarioDAO.findByRol(rol);
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createUsuario(Usuario usuario) {
        try {
            // Validar que no exista el nombre de usuario
            if (usuarioDAO.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
                return Response
                        .status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"El nombre de usuario ya existe\"}")
                        .build();
            }
            
            // Validar que no exista el email
            if (usuarioDAO.findByEmail(usuario.getEmail()).isPresent()) {
                return Response
                        .status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"El correo electrónico ya está registrado\"}")
                        .build();
            }
            
            // Hashear la contraseña (el frontend envía la contraseña en contrasenaHash)
            String passwordHash = BCrypt.hashpw(usuario.getContrasenaHash(), BCrypt.gensalt());
            usuario.setContrasenaHash(passwordHash);
            
            // Guardar usuario
            Usuario nuevoUsuario = usuarioDAO.save(usuario);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(nuevoUsuario)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateUsuario(@PathParam("id") Integer id, Usuario usuario) {
        try {
            // Verificar que el usuario exista
            Usuario usuarioExistente = usuarioDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Actualizar campos
            if (usuario.getNombreUsuario() != null) {
                usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
            }
            if (usuario.getEmail() != null) {
                usuarioExistente.setEmail(usuario.getEmail());
            }
            if (usuario.getRol() != null) {
                usuarioExistente.setRol(usuario.getRol());
            }
            if (usuario.getActivo() != null) {
                usuarioExistente.setActivo(usuario.getActivo());
            }
            
            // Si se envía una nueva contraseña, hashearla
            if (usuario.getContrasenaHash() != null && !usuario.getContrasenaHash().isEmpty()) {
                String passwordHash = BCrypt.hashpw(usuario.getContrasenaHash(), BCrypt.gensalt());
                usuarioExistente.setContrasenaHash(passwordHash);
            }
            
            Usuario usuarioActualizado = usuarioDAO.save(usuarioExistente);
            return Response.ok(usuarioActualizado).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteUsuario(@PathParam("id") Integer id) {
        try {
            usuarioDAO.delete(id);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
