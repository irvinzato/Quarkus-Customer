package com.rivera;

import com.rivera.entities.Customer;
import com.rivera.repositories.RepositoryCustomer;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
public class ControllerCustomer {

    @Inject
    private RepositoryCustomer repositoryCustomer;

    @GET
    public List<Customer> getCustomers() {
        return repositoryCustomer.listCustomer();
    }

    @POST
    public Response createCustomer(Customer customer){
        repositoryCustomer.createCustomer(customer);
        return Response.accepted().build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id){
        Customer customer = repositoryCustomer.byId(id);
        return Response.ok(customer).build();
    }

    @DELETE
    @Path("/borrar/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        repositoryCustomer.deleteCustomer(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/editar/{id}")
    public Response editCustomer(@PathParam("id") Long id, Customer customer) {
        repositoryCustomer.editCustomer(id, customer);
        return Response.ok("Cliente actualizado correctamente").build();
    }


}