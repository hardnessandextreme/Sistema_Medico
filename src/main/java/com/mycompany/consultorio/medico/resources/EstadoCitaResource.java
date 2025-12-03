package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.EstadoCitaDAO;
import com.mycompany.consultorio.medico.model.EstadoCita;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("estados-cita")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstadoCitaResource {
    
    private final EstadoCitaDAO estadoCitaDAO = new EstadoCitaDAO();
    
    @GET
    public Response getAllEstados() {
        try {
            List<EstadoCita> estados = estadoCitaDAO.findAll();
            return Response.ok(estados).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getEstadoById(@PathParam("id") Integer id) {
        try {
            return estadoCitaDAO.findById(id)
                    .map(estado -> Response.ok(estado).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Estado no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("nombre/{nombre}")
    public Response getEstadoByNombre(@PathParam("nombre") String nombre) {
        try {
            return estadoCitaDAO.findByNombre(nombre)
                    .map(estado -> Response.ok(estado).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Estado no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createEstado(EstadoCita estado) {
        try {
            EstadoCita savedEstado = estadoCitaDAO.save(estado);
            return Response.status(Response.Status.CREATED).entity(savedEstado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateEstado(@PathParam("id") Integer id, EstadoCita estado) {
        try {
            estado.setIdEstadoCita(id);
            EstadoCita updatedEstado = estadoCitaDAO.save(estado);
            return Response.ok(updatedEstado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteEstado(@PathParam("id") Integer id) {
        try {
            estadoCitaDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
