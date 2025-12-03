package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.CitaDAO;
import com.mycompany.consultorio.medico.dao.EstadoCitaDAO;
import com.mycompany.consultorio.medico.dao.PacienteDAO;
import com.mycompany.consultorio.medico.dao.MedicoDAO;
import com.mycompany.consultorio.medico.model.Cita;
import com.mycompany.consultorio.medico.model.EstadoCita;
import com.mycompany.consultorio.medico.model.Paciente;
import com.mycompany.consultorio.medico.model.Medico;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaResource {
    
    private final CitaDAO citaDAO = new CitaDAO();
    private final EstadoCitaDAO estadoCitaDAO = new EstadoCitaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @GET
    public Response getAllCitas() {
        try {
            List<Cita> citas = citaDAO.findAll();
            return Response.ok(citas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getCitaById(@PathParam("id") Integer id) {
        try {
            return citaDAO.findById(id)
                    .map(cita -> Response.ok(cita).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("fecha")
    public Response getCitasByFecha(@QueryParam("inicio") String inicio, @QueryParam("fin") String fin) {
        try {
            LocalDate fechaInicio = LocalDate.parse(inicio, formatter);
            LocalDate fechaFin = LocalDate.parse(fin, formatter);
            List<Cita> citas = citaDAO.findByFechaRange(fechaInicio, fechaFin);
            return Response.ok(citas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("paciente/{idPaciente}")
    public Response getCitasByPaciente(@PathParam("idPaciente") Integer idPaciente) {
        try {
            return pacienteDAO.findById(idPaciente)
                    .map(paciente -> {
                        List<Cita> citas = citaDAO.findByPaciente(paciente);
                        return Response.ok(citas).build();
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
    public Response getCitasByMedico(@PathParam("idMedico") Integer idMedico) {
        try {
            return medicoDAO.findById(idMedico)
                    .map(medico -> {
                        List<Cita> citas = citaDAO.findByMedico(medico);
                        return Response.ok(citas).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"M\u00e9dico no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createCita(Cita cita) {
        try {
            Cita savedCita = citaDAO.save(cita);
            return Response.status(Response.Status.CREATED).entity(savedCita).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateCita(@PathParam("id") Integer id, Cita cita) {
        try {
            cita.setIdCita(id);
            Cita updatedCita = citaDAO.save(cita);
            return Response.ok(updatedCita).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteCita(@PathParam("id") Integer id) {
        try {
            citaDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
