package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.ArchivoMedicoDAO;
import com.mycompany.consultorio.medico.dao.PacienteDAO;
import com.mycompany.consultorio.medico.dao.ConsultaDAO;
import com.mycompany.consultorio.medico.model.ArchivoMedico;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("archivos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArchivoMedicoResource {
    
    private final ArchivoMedicoDAO archivoDAO = new ArchivoMedicoDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    @GET
    public Response getAllArchivos(@QueryParam("tipo") String tipo) {
        try {
            List<ArchivoMedico> archivos;
            if (tipo != null && !tipo.isEmpty()) {
                archivos = archivoDAO.findByTipo(tipo);
            } else {
                archivos = archivoDAO.findAll();
            }
            return Response.ok(archivos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getArchivoById(@PathParam("id") Integer id) {
        try {
            return archivoDAO.findById(id)
                    .map(archivo -> Response.ok(archivo).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Archivo no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("paciente/{idPaciente}")
    public Response getArchivosByPaciente(
            @PathParam("idPaciente") Integer idPaciente,
            @QueryParam("tipo") String tipo) {
        try {
            return pacienteDAO.findById(idPaciente)
                    .map(paciente -> {
                        List<ArchivoMedico> archivos;
                        if (tipo != null && !tipo.isEmpty()) {
                            archivos = archivoDAO.findByPacienteAndTipo(paciente, tipo);
                        } else {
                            archivos = archivoDAO.findByPaciente(paciente);
                        }
                        return Response.ok(archivos).build();
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
    @Path("consulta/{idConsulta}")
    public Response getArchivosByConsulta(@PathParam("idConsulta") Integer idConsulta) {
        try {
            return consultaDAO.findById(idConsulta)
                    .map(consulta -> {
                        List<ArchivoMedico> archivos = archivoDAO.findByConsulta(consulta);
                        return Response.ok(archivos).build();
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
    @Path("fecha")
    public Response getArchivosByFechaRange(
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
            
            List<ArchivoMedico> archivos = archivoDAO.findByFechaRange(fechaInicio, fechaFin);
            return Response.ok(archivos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createArchivo(ArchivoMedico archivo) {
        try {
            ArchivoMedico savedArchivo = archivoDAO.save(archivo);
            return Response.status(Response.Status.CREATED).entity(savedArchivo).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateArchivo(@PathParam("id") Integer id, ArchivoMedico archivo) {
        try {
            archivo.setIdArchivo(id);
            ArchivoMedico updatedArchivo = archivoDAO.save(archivo);
            return Response.ok(updatedArchivo).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteArchivo(@PathParam("id") Integer id) {
        try {
            archivoDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
