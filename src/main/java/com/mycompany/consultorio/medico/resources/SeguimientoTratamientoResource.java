package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.SeguimientoTratamientoDAO;
import com.mycompany.consultorio.medico.dao.TratamientoDAO;
import com.mycompany.consultorio.medico.dao.PacienteDAO;
import com.mycompany.consultorio.medico.model.SeguimientoTratamiento;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("seguimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeguimientoTratamientoResource {
    
    private final SeguimientoTratamientoDAO seguimientoDAO = new SeguimientoTratamientoDAO();
    private final TratamientoDAO tratamientoDAO = new TratamientoDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @GET
    public Response getAllSeguimientos(@QueryParam("cumpliendo") String cumpliendo) {
        try {
            List<SeguimientoTratamiento> seguimientos;
            if (cumpliendo != null && !cumpliendo.isEmpty()) {
                seguimientos = seguimientoDAO.findByCumpliendoTratamiento(cumpliendo);
            } else {
                seguimientos = seguimientoDAO.findAll();
            }
            return Response.ok(seguimientos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getSeguimientoById(@PathParam("id") Integer id) {
        try {
            return seguimientoDAO.findById(id)
                    .map(seguimiento -> Response.ok(seguimiento).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Seguimiento no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("tratamiento/{idTratamiento}")
    public Response getSeguimientosByTratamiento(@PathParam("idTratamiento") Integer idTratamiento) {
        try {
            return tratamientoDAO.findById(idTratamiento)
                    .map(tratamiento -> {
                        List<SeguimientoTratamiento> seguimientos = seguimientoDAO.findByTratamiento(tratamiento);
                        return Response.ok(seguimientos).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Tratamiento no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("paciente/{idPaciente}")
    public Response getSeguimientosByPaciente(
            @PathParam("idPaciente") Integer idPaciente,
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr) {
        try {
            return pacienteDAO.findById(idPaciente)
                    .map(paciente -> {
                        List<SeguimientoTratamiento> seguimientos;
                        if (fechaInicioStr != null && fechaFinStr != null) {
                            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, DATE_FORMATTER);
                            LocalDate fechaFin = LocalDate.parse(fechaFinStr, DATE_FORMATTER);
                            seguimientos = seguimientoDAO.findByPacienteAndFechaRange(paciente, fechaInicio, fechaFin);
                        } else {
                            seguimientos = seguimientoDAO.findByPaciente(paciente);
                        }
                        return Response.ok(seguimientos).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Paciente no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createSeguimiento(SeguimientoTratamiento seguimiento) {
        try {
            SeguimientoTratamiento savedSeguimiento = seguimientoDAO.save(seguimiento);
            return Response.status(Response.Status.CREATED).entity(savedSeguimiento).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateSeguimiento(@PathParam("id") Integer id, SeguimientoTratamiento seguimiento) {
        try {
            seguimiento.setIdSeguimiento(id);
            SeguimientoTratamiento updatedSeguimiento = seguimientoDAO.save(seguimiento);
            return Response.ok(updatedSeguimiento).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteSeguimiento(@PathParam("id") Integer id) {
        try {
            seguimientoDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
