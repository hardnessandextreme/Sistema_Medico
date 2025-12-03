package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.ConsultaDAO;
import com.mycompany.consultorio.medico.dao.PacienteDAO;
import com.mycompany.consultorio.medico.dao.MedicoDAO;
import com.mycompany.consultorio.medico.model.Consulta;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource {
    
    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    @GET
    public Response getAllConsultas() {
        try {
            List<Consulta> consultas = consultaDAO.findAll();
            return Response.ok(consultas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getConsultaById(@PathParam("id") Integer id) {
        try {
            return consultaDAO.findById(id)
                    .map(consulta -> Response.ok(consulta).build())
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
    @Path("paciente/{idPaciente}")
    public Response getConsultasByPaciente(@PathParam("idPaciente") Integer idPaciente) {
        try {
            return pacienteDAO.findById(idPaciente)
                    .map(paciente -> {
                        List<Consulta> consultas = consultaDAO.findByPaciente(paciente);
                        return Response.ok(consultas).build();
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
    
    @GET
    @Path("medico/{idMedico}")
    public Response getConsultasByMedico(@PathParam("idMedico") Integer idMedico) {
        try {
            return medicoDAO.findById(idMedico)
                    .map(medico -> {
                        List<Consulta> consultas = consultaDAO.findByMedico(medico);
                        return Response.ok(consultas).build();
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
    @Path("fecha")
    public Response getConsultasByFecha(@QueryParam("inicio") String inicio, @QueryParam("fin") String fin) {
        try {
            LocalDateTime fechaInicio = LocalDateTime.parse(inicio, formatter);
            LocalDateTime fechaFin = LocalDateTime.parse(fin, formatter);
            List<Consulta> consultas = consultaDAO.findByFechaRange(fechaInicio, fechaFin);
            return Response.ok(consultas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createConsulta(Consulta consulta) {
        try {
            Consulta savedConsulta = consultaDAO.save(consulta);
            return Response.status(Response.Status.CREATED).entity(savedConsulta).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateConsulta(@PathParam("id") Integer id, Consulta consulta) {
        try {
            consulta.setIdConsulta(id);
            Consulta updatedConsulta = consultaDAO.save(consulta);
            return Response.ok(updatedConsulta).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteConsulta(@PathParam("id") Integer id) {
        try {
            consultaDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
