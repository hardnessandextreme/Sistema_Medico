package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.AuditoriaDAO;
import com.mycompany.consultorio.medico.dao.UsuarioDAO;
import com.mycompany.consultorio.medico.model.Auditoria;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("auditorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuditoriaResource {
    
    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    @GET
    public Response getAllAuditorias(
            @QueryParam("tabla") String tabla,
            @QueryParam("operacion") String operacion,
            @QueryParam("limite") @DefaultValue("100") int limite) {
        try {
            List<Auditoria> auditorias;
            
            if (tabla != null && !tabla.isEmpty()) {
                auditorias = auditoriaDAO.findByTabla(tabla);
            } else if (operacion != null && !operacion.isEmpty()) {
                auditorias = auditoriaDAO.findByOperacion(operacion);
            } else {
                auditorias = auditoriaDAO.findRecientes(limite);
            }
            
            return Response.ok(auditorias).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getAuditoriaById(@PathParam("id") Integer id) {
        try {
            return auditoriaDAO.findById(id)
                    .map(auditoria -> Response.ok(auditoria).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Auditoría no encontrada\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("usuario/{idUsuario}")
    public Response getAuditoriasByUsuario(@PathParam("idUsuario") Integer idUsuario) {
        try {
            return usuarioDAO.findById(idUsuario)
                    .map(usuario -> {
                        List<Auditoria> auditorias = auditoriaDAO.findByUsuario(usuario);
                        return Response.ok(auditorias).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Usuario no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("registro/{tabla}/{registroId}")
    public Response getAuditoriasByRegistro(
            @PathParam("tabla") String tabla,
            @PathParam("registroId") Integer registroId) {
        try {
            List<Auditoria> auditorias = auditoriaDAO.findByTablaAndRegistro(tabla, registroId);
            return Response.ok(auditorias).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("fecha")
    public Response getAuditoriasByFechaRange(
            @QueryParam("inicio") String fechaInicioStr,
            @QueryParam("fin") String fechaFinStr) {
        try {
            if (fechaInicioStr == null || fechaFinStr == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Se requieren parámetros inicio y fin\"}")
                        .build();
            }
            
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, DATETIME_FORMATTER);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, DATETIME_FORMATTER);
            
            List<Auditoria> auditorias = auditoriaDAO.findByFechaRange(fechaInicio, fechaFin);
            return Response.ok(auditorias).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createAuditoria(Auditoria auditoria) {
        try {
            Auditoria savedAuditoria = auditoriaDAO.save(auditoria);
            return Response.status(Response.Status.CREATED).entity(savedAuditoria).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateAuditoria(@PathParam("id") Integer id, Auditoria auditoria) {
        try {
            auditoria.setIdAuditoria(id);
            Auditoria updatedAuditoria = auditoriaDAO.save(auditoria);
            return Response.ok(updatedAuditoria).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteAuditoria(@PathParam("id") Integer id) {
        try {
            auditoriaDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
