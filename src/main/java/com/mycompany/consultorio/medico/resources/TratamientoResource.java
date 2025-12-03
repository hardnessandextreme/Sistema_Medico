package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.TratamientoDAO;
import com.mycompany.consultorio.medico.dao.ConsultaDAO;
import com.mycompany.consultorio.medico.dao.MedicamentoDAO;
import com.mycompany.consultorio.medico.model.Tratamiento;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("tratamientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TratamientoResource {
    
    private final TratamientoDAO tratamientoDAO = new TratamientoDAO();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
    
    @GET
    public Response getAllTratamientos(@QueryParam("activos") Boolean activos) {
        try {
            List<Tratamiento> tratamientos;
            if (activos != null && activos) {
                tratamientos = tratamientoDAO.findActivos();
            } else {
                tratamientos = tratamientoDAO.findAll();
            }
            return Response.ok(tratamientos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getTratamientoById(@PathParam("id") Integer id) {
        try {
            return tratamientoDAO.findById(id)
                    .map(tratamiento -> Response.ok(tratamiento).build())
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
    @Path("consulta/{idConsulta}")
    public Response getTratamientosByConsulta(@PathParam("idConsulta") Integer idConsulta) {
        try {
            return consultaDAO.findById(idConsulta)
                    .map(consulta -> {
                        List<Tratamiento> tratamientos = tratamientoDAO.findByConsulta(consulta);
                        return Response.ok(tratamientos).build();
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
    @Path("medicamento/{idMedicamento}")
    public Response getTratamientosByMedicamento(@PathParam("idMedicamento") Integer idMedicamento) {
        try {
            return medicamentoDAO.findById(idMedicamento)
                    .map(medicamento -> {
                        List<Tratamiento> tratamientos = tratamientoDAO.findByMedicamento(medicamento);
                        return Response.ok(tratamientos).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Medicamento no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("paciente/{idPaciente}/activos")
    public Response getTratamientosActivosByPaciente(@PathParam("idPaciente") Integer idPaciente) {
        try {
            List<Tratamiento> tratamientos = tratamientoDAO.findActivosByPaciente(idPaciente);
            return Response.ok(tratamientos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createTratamiento(Tratamiento tratamiento) {
        try {
            Tratamiento savedTratamiento = tratamientoDAO.save(tratamiento);
            return Response.status(Response.Status.CREATED).entity(savedTratamiento).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateTratamiento(@PathParam("id") Integer id, Tratamiento tratamiento) {
        try {
            tratamiento.setIdTratamiento(id);
            Tratamiento updatedTratamiento = tratamientoDAO.save(tratamiento);
            return Response.ok(updatedTratamiento).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteTratamiento(@PathParam("id") Integer id) {
        try {
            tratamientoDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
