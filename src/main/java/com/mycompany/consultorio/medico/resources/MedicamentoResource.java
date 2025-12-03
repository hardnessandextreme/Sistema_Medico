package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.MedicamentoDAO;
import com.mycompany.consultorio.medico.model.Medicamento;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("medicamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicamentoResource {
    
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
    
    @GET
    public Response getAllMedicamentos(@QueryParam("activos") Boolean activos) {
        try {
            List<Medicamento> medicamentos;
            if (activos != null && activos) {
                medicamentos = medicamentoDAO.findAllActivos();
            } else {
                medicamentos = medicamentoDAO.findAll();
            }
            return Response.ok(medicamentos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getMedicamentoById(@PathParam("id") Integer id) {
        try {
            return medicamentoDAO.findById(id)
                    .map(medicamento -> Response.ok(medicamento).build())
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
    @Path("search/{termino}")
    public Response searchMedicamentos(@PathParam("termino") String termino) {
        try {
            List<Medicamento> medicamentos = medicamentoDAO.searchByNombre(termino);
            return Response.ok(medicamentos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createMedicamento(Medicamento medicamento) {
        try {
            Medicamento savedMedicamento = medicamentoDAO.save(medicamento);
            return Response.status(Response.Status.CREATED).entity(savedMedicamento).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateMedicamento(@PathParam("id") Integer id, Medicamento medicamento) {
        try {
            medicamento.setIdMedicamento(id);
            Medicamento updatedMedicamento = medicamentoDAO.save(medicamento);
            return Response.ok(updatedMedicamento).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteMedicamento(@PathParam("id") Integer id) {
        try {
            medicamentoDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
