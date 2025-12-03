package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.ExamenLaboratorioDAO;
import com.mycompany.consultorio.medico.dao.ConsultaDAO;
import com.mycompany.consultorio.medico.model.ExamenLaboratorio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("examenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExamenLaboratorioResource {
    
    private final ExamenLaboratorioDAO examenDAO = new ExamenLaboratorioDAO();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    
    @GET
    public Response getAllExamenes(@QueryParam("estado") String estado) {
        try {
            List<ExamenLaboratorio> examenes;
            if (estado != null && !estado.isEmpty()) {
                examenes = examenDAO.findByEstado(estado);
            } else {
                examenes = examenDAO.findAll();
            }
            return Response.ok(examenes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getExamenById(@PathParam("id") Integer id) {
        try {
            return examenDAO.findById(id)
                    .map(examen -> Response.ok(examen).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Examen no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("consulta/{idConsulta}")
    public Response getExamenesByConsulta(@PathParam("idConsulta") Integer idConsulta) {
        try {
            return consultaDAO.findById(idConsulta)
                    .map(consulta -> {
                        List<ExamenLaboratorio> examenes = examenDAO.findByConsulta(consulta);
                        return Response.ok(examenes).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Consulta no encontrada\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("pendientes")
    public Response getExamenesPendientes() {
        try {
            List<ExamenLaboratorio> examenes = examenDAO.findPendientes();
            return Response.ok(examenes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createExamen(ExamenLaboratorio examen) {
        try {
            ExamenLaboratorio savedExamen = examenDAO.save(examen);
            return Response.status(Response.Status.CREATED).entity(savedExamen).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateExamen(@PathParam("id") Integer id, ExamenLaboratorio examen) {
        try {
            examen.setIdExamen(id);
            ExamenLaboratorio updatedExamen = examenDAO.save(examen);
            return Response.ok(updatedExamen).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteExamen(@PathParam("id") Integer id) {
        try {
            examenDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
