package com.mycompany.consultorio.medico.resources;

import com.mycompany.consultorio.medico.dao.RolDAO;
import com.mycompany.consultorio.medico.model.Rol;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RolResource {
    
    private final RolDAO rolDAO = new RolDAO();
    
    @GET
    public Response getAllRoles() {
        try {
            List<Rol> roles = rolDAO.findAll();
            return Response.ok(roles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response getRolById(@PathParam("id") Integer id) {
        try {
            return rolDAO.findById(id)
                    .map(rol -> Response.ok(rol).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Rol no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("nombre/{nombre}")
    public Response getRolByNombre(@PathParam("nombre") String nombre) {
        try {
            return rolDAO.findByNombreRol(nombre)
                    .map(rol -> Response.ok(rol).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Rol no encontrado\"}")
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @POST
    public Response createRol(Rol rol) {
        try {
            Rol savedRol = rolDAO.save(rol);
            return Response.status(Response.Status.CREATED).entity(savedRol).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response updateRol(@PathParam("id") Integer id, Rol rol) {
        try {
            rol.setIdRol(id);
            Rol updatedRol = rolDAO.save(rol);
            return Response.ok(updatedRol).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteRol(@PathParam("id") Integer id) {
        try {
            rolDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
