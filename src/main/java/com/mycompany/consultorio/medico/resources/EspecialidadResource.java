package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.EspecialidadDAO;
import com.mycompany.consultorio.medico.model.Especialidad;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("especialidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EspecialidadResource {
    
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    
    @GET
    public Response getAllEspecialidades(@QueryParam("activas") Boolean activas) {
        try {
            List<Especialidad> especialidades;
            if (activas != null && activas) {
                especialidades = especialidadDAO.findAllActivas();
            } else {
                especialidades = especialidadDAO.findAll();
            }
            return Response.ok(especialidades).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getEspecialidadById(@PathParam("id") Integer id) {
        try {
            return especialidadDAO.findById(id)
                    .map(especialidad -> Response.ok(especialidad).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Especialidad no encontrada\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("nombre/{nombre}")
    public Response getEspecialidadByNombre(@PathParam("nombre") String nombre) {
        try {
            return especialidadDAO.findByNombre(nombre)
                    .map(especialidad -> Response.ok(especialidad).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Especialidad no encontrada\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createEspecialidad(Especialidad especialidad) {
        try {
            Especialidad savedEspecialidad = especialidadDAO.save(especialidad);
            return Response.status(Response.Status.CREATED).entity(savedEspecialidad).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateEspecialidad(@PathParam("id") Integer id, Especialidad especialidad) {
        try {
            especialidad.setIdEspecialidad(id);
            Especialidad updatedEspecialidad = especialidadDAO.save(especialidad);
            return Response.ok(updatedEspecialidad).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteEspecialidad(@PathParam("id") Integer id) {
        try {
            especialidadDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
