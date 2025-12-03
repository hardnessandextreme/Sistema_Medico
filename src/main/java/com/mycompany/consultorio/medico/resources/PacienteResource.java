package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.PacienteDAO;
import com.mycompany.consultorio.medico.dao.UsuarioDAO;
import com.mycompany.consultorio.medico.model.Paciente;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

@Path("pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {
    
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @GET
    public Response getAllPacientes() {
        try {
            List<Paciente> pacientes = pacienteDAO.findAll();
            return Response.ok(pacientes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getPacienteById(@PathParam("id") Integer id) {
        try {
            return pacienteDAO.findById(id)
                    .map(paciente -> Response.ok(paciente).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("cedula/{cedula}")
    public Response getPacienteByCedula(@PathParam("cedula") String cedula) {
        try {
            return pacienteDAO.findByCedula(cedula)
                    .map(paciente -> Response.ok(paciente).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("usuario/{idUsuario}")
    public Response getPacienteByUsuarioId(@PathParam("idUsuario") Integer idUsuario) {
        try {
            return pacienteDAO.findByUsuarioId(idUsuario)
                    .map(paciente -> Response.ok(paciente).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Paciente no encontrado para este usuario\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("search/{term}")
    public Response searchPacientes(@PathParam("term") String term) {
        try {
            List<Paciente> pacientes = pacienteDAO.searchByName(term);
            return Response.ok(pacientes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createPaciente(Paciente paciente) {
        System.out.println("========== CREAR PACIENTE ==========");
        System.out.println("Paciente recibido: " + paciente);
        System.out.println("Cedula: " + (paciente != null ? paciente.getCedula() : "null"));
        System.out.println("Nombres: " + (paciente != null ? paciente.getNombres() : "null"));
        System.out.println("Usuario: " + (paciente != null && paciente.getUsuario() != null ? paciente.getUsuario().getNombreUsuario() : "null"));
        System.out.println("Email: " + (paciente != null && paciente.getUsuario() != null ? paciente.getUsuario().getEmail() : "null"));
        System.out.println("Rol: " + (paciente != null && paciente.getUsuario() != null && paciente.getUsuario().getRol() != null ? paciente.getUsuario().getRol().getIdRol() : "null"));
        
        try {
            // Validar que la cédula no exista
            if (pacienteDAO.findByCedula(paciente.getCedula()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"La cédula ya está registrada\"}")
                        .build();
            }
            
            // Validar que el email no exista
            if (paciente.getUsuario() != null && paciente.getUsuario().getEmail() != null) {
                if (usuarioDAO.findByEmail(paciente.getUsuario().getEmail()).isPresent()) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("{\"error\": \"El correo electrónico ya está registrado\"}")
                            .build();
                }
            }
            // Hash password if not already hashed
            if (paciente.getUsuario() != null && paciente.getUsuario().getContrasenaHash() != null) {
                String plainPassword = paciente.getUsuario().getContrasenaHash();
                System.out.println("Contraseña recibida (length): " + plainPassword.length());
                if (!plainPassword.startsWith("$2a$")) {
                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                    paciente.getUsuario().setContrasenaHash(hashedPassword);
                    System.out.println("Contraseña hasheada");
                }
            }
            
            System.out.println("Guardando paciente...");
            Paciente savedPaciente = pacienteDAO.save(paciente);
            System.out.println("Paciente guardado con ID: " + savedPaciente.getIdPaciente());
            return Response.status(Response.Status.CREATED).entity(savedPaciente).build();
        } catch (Exception e) {
            System.err.println("ERROR al crear paciente:");
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\", \"details\": \"" + e.getClass().getName() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updatePaciente(@PathParam("id") Integer id, Paciente paciente) {
        try {
            // Validar que la cédula no exista en otro paciente
            pacienteDAO.findByCedula(paciente.getCedula()).ifPresent(p -> {
                if (!p.getIdPaciente().equals(id)) {
                    throw new RuntimeException("La cédula ya está registrada en otro paciente");
                }
            });
            
            paciente.setIdPaciente(id);
            Paciente updatedPaciente = pacienteDAO.save(paciente);
            return Response.ok(updatedPaciente).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deletePaciente(@PathParam("id") Integer id) {
        try {
            pacienteDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
