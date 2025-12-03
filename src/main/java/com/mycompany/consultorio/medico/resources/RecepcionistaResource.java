package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.RecepcionistaDAO;
import com.mycompany.consultorio.medico.dao.UsuarioDAO;
import com.mycompany.consultorio.medico.model.Recepcionista;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

@Path("recepcionistas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecepcionistaResource {
    
    private final RecepcionistaDAO recepcionistaDAO = new RecepcionistaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @GET
    public Response getAllRecepcionistas(@QueryParam("activos") Boolean activos) {
        try {
            // Por defecto devolver TODOS (activos e inactivos)
            List<Recepcionista> recepcionistas;
            if (activos != null && activos) {
                recepcionistas = recepcionistaDAO.findAllActivos();
            } else {
                recepcionistas = recepcionistaDAO.findAll();
            }
            return Response.ok(recepcionistas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getRecepcionistaById(@PathParam("id") Integer id) {
        try {
            return recepcionistaDAO.findById(id)
                    .map(recepcionista -> Response.ok(recepcionista).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Recepcionista no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("cedula/{cedula}")
    public Response getRecepcionistaByCedula(@PathParam("cedula") String cedula) {
        try {
            return recepcionistaDAO.findByCedula(cedula)
                    .map(recepcionista -> Response.ok(recepcionista).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Recepcionista no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("search/{termino}")
    public Response searchRecepcionistas(@PathParam("termino") String termino) {
        try {
            List<Recepcionista> recepcionistas = recepcionistaDAO.searchByName(termino);
            return Response.ok(recepcionistas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createRecepcionista(Recepcionista recepcionista) {
        try {
            // Validar que la cédula no exista
            if (recepcionistaDAO.findByCedula(recepcionista.getCedula()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"La cédula ya está registrada\"}")
                        .build();
            }
            
            // Validar que el email no exista
            if (recepcionista.getUsuario() != null && recepcionista.getUsuario().getEmail() != null) {
                if (usuarioDAO.findByEmail(recepcionista.getUsuario().getEmail()).isPresent()) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("{\"error\": \"El correo electrónico ya está registrado\"}")
                            .build();
                }
            }
            
            // Hash password if not already hashed
            if (recepcionista.getUsuario() != null && recepcionista.getUsuario().getContrasenaHash() != null) {
                String plainPassword = recepcionista.getUsuario().getContrasenaHash();
                if (!plainPassword.startsWith("$2a$")) {
                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                    recepcionista.getUsuario().setContrasenaHash(hashedPassword);
                }
            }
            Recepcionista savedRecepcionista = recepcionistaDAO.save(recepcionista);
            return Response.status(Response.Status.CREATED).entity(savedRecepcionista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateRecepcionista(@PathParam("id") Integer id, Recepcionista recepcionista) {
        try {
            // Obtener la recepcionista existente para preservar la relación con usuario
            Recepcionista recepcionistaExistente = recepcionistaDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Recepcionista no encontrado"));
            
            // Validar que la cédula no exista en otra recepcionista
            recepcionistaDAO.findByCedula(recepcionista.getCedula()).ifPresent(r -> {
                if (!r.getIdRecepcionista().equals(id)) {
                    throw new RuntimeException("La cédula ya está registrada en otra recepcionista");
                }
            });
            
            // Actualizar solo los campos que vienen en el request
            recepcionistaExistente.setCedula(recepcionista.getCedula());
            recepcionistaExistente.setNombres(recepcionista.getNombres());
            recepcionistaExistente.setApellidos(recepcionista.getApellidos());
            recepcionistaExistente.setFechaNacimiento(recepcionista.getFechaNacimiento());
            recepcionistaExistente.setTelefono(recepcionista.getTelefono());
            recepcionistaExistente.setDireccion(recepcionista.getDireccion());
            recepcionistaExistente.setFechaContratacion(recepcionista.getFechaContratacion());
            recepcionistaExistente.setActivo(recepcionista.getActivo());
            // NO actualizar usuario - se preserva el existente
            
            Recepcionista updatedRecepcionista = recepcionistaDAO.save(recepcionistaExistente);
            return Response.ok(updatedRecepcionista).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteRecepcionista(@PathParam("id") Integer id) {
        try {
            recepcionistaDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
