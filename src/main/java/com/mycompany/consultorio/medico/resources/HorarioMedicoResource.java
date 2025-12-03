package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.HorarioMedicoDAO;
import com.mycompany.consultorio.medico.dao.MedicoDAO;
import com.mycompany.consultorio.medico.model.HorarioMedico;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("horarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HorarioMedicoResource {
    
    private final HorarioMedicoDAO horarioDAO = new HorarioMedicoDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    
    @GET
    public Response getAllHorarios() {
        try {
            List<HorarioMedico> horarios = horarioDAO.findAll();
            return Response.ok(horarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getHorarioById(@PathParam("id") Integer id) {
        try {
            return horarioDAO.findById(id)
                    .map(horario -> Response.ok(horario).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Horario no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("medico/{idMedico}")
    public Response getHorariosByMedico(@PathParam("idMedico") Integer idMedico, 
                                        @QueryParam("activos") Boolean activos) {
        try {
            return medicoDAO.findById(idMedico)
                    .map(medico -> {
                        List<HorarioMedico> horarios;
                        if (activos != null && activos) {
                            horarios = horarioDAO.findByMedicoActivos(medico);
                        } else {
                            horarios = horarioDAO.findByMedico(medico);
                        }
                        return Response.ok(horarios).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Médico no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("dia/{dia}")
    public Response getHorariosByDia(@PathParam("dia") Integer dia) {
        try {
            if (dia < 0 || dia > 6) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El día debe estar entre 0 (Domingo) y 6 (Sábado)\"}")
                        .build();
            }
            List<HorarioMedico> horarios = horarioDAO.findByDiaSemana(dia);
            return Response.ok(horarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createHorario(HorarioMedico horario) {
        try {
            HorarioMedico savedHorario = horarioDAO.save(horario);
            return Response.status(Response.Status.CREATED).entity(savedHorario).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateHorario(@PathParam("id") Integer id, HorarioMedico horario) {
        try {
            horario.setIdHorario(id);
            HorarioMedico updatedHorario = horarioDAO.save(horario);
            return Response.ok(updatedHorario).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteHorario(@PathParam("id") Integer id) {
        try {
            horarioDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
