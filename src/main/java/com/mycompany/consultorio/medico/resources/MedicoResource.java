package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.MedicoDAO;
import com.mycompany.consultorio.medico.dao.UsuarioDAO;
import com.mycompany.consultorio.medico.model.Medico;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

@Path("medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicoResource {
    
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @GET
    public Response getAllMedicos() {
        try {
            List<Medico> medicos = medicoDAO.findAll();
            return Response.ok(medicos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getMedicoById(@PathParam("id") Integer id) {
        try {
            return medicoDAO.findById(id)
                    .map(medico -> Response.ok(medico).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("cedula/{cedula}")
    public Response getMedicoByCedula(@PathParam("cedula") String cedula) {
        try {
            return medicoDAO.findByCedula(cedula)
                    .map(medico -> Response.ok(medico).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("especialidad/{idEspecialidad}")
    public Response getMedicosByEspecialidad(@PathParam("idEspecialidad") Integer idEspecialidad) {
        try {
            List<Medico> medicos = medicoDAO.findByEspecialidadId(idEspecialidad);
            return Response.ok(medicos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createMedico(Medico medico) {
        try {
            // Validar que la cédula no exista
            if (medicoDAO.findByCedula(medico.getCedula()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"La cédula ya está registrada\"}")
                        .build();
            }
            
            // Validar que el email no exista
            if (medico.getUsuario() != null && medico.getUsuario().getEmail() != null) {
                if (usuarioDAO.findByEmail(medico.getUsuario().getEmail()).isPresent()) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("{\"error\": \"El correo electrónico ya está registrado\"}")
                            .build();
                }
            }
            
            // Si el médico viene con un usuario nuevo, hashear la contraseña
            if (medico.getUsuario() != null && medico.getUsuario().getContrasenaHash() != null) {
                String plainPassword = medico.getUsuario().getContrasenaHash();
                // Solo hashear si no está ya hasheada (no comienza con $2a$)
                if (!plainPassword.startsWith("$2a$")) {
                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                    medico.getUsuario().setContrasenaHash(hashedPassword);
                }
            }
            
            Medico savedMedico = medicoDAO.save(medico);
            return Response.status(Response.Status.CREATED).entity(savedMedico).build();
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateMedico(@PathParam("id") Integer id, Medico medico) {
        try {
            // Obtener el médico existente para preservar la relación con usuario
            Medico medicoExistente = medicoDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
            
            // Validar que la cédula no exista en otro médico
            medicoDAO.findByCedula(medico.getCedula()).ifPresent(m -> {
                if (!m.getIdMedico().equals(id)) {
                    throw new RuntimeException("La cédula ya está registrada en otro médico");
                }
            });
            
            // Actualizar solo los campos que vienen en el request
            medicoExistente.setCedula(medico.getCedula());
            medicoExistente.setNombres(medico.getNombres());
            medicoExistente.setApellidos(medico.getApellidos());
            medicoExistente.setEspecialidad(medico.getEspecialidad());
            medicoExistente.setFechaNacimiento(medico.getFechaNacimiento());
            medicoExistente.setNumeroLicencia(medico.getNumeroLicencia());
            medicoExistente.setTelefono(medico.getTelefono());
            medicoExistente.setDireccion(medico.getDireccion());
            medicoExistente.setFotoUrl(medico.getFotoUrl());
            medicoExistente.setFechaContratacion(medico.getFechaContratacion());
            medicoExistente.setActivo(medico.getActivo());
            // NO actualizar usuario - se preserva el existente
            
            Medico updatedMedico = medicoDAO.save(medicoExistente);
            return Response.ok(updatedMedico).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteMedico(@PathParam("id") Integer id) {
        try {
            medicoDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
