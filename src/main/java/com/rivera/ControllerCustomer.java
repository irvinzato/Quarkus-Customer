package com.rivera;

import com.rivera.comunicationotherservices.CommunicationServiceProduct;
import com.rivera.entities.Customer;
import com.rivera.repositories.RepositoryCustomer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

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

    @Inject
    private CommunicationServiceProduct communicationServiceProduct;

    @GET
    @Blocking //Para evitar error, ya que es reactivo pero la base de datos no lo es, por lo tanto es bloqueante
    public List<Customer> getCustomers() {
        return repositoryCustomer.listCustomer();
    }

    @POST
    @Blocking //Para evitar error, ya que es reactivo pero la base de datos no lo es, por lo tanto es bloqueante
    public Response createCustomer(Customer customer){
        repositoryCustomer.createCustomer(customer);
        return Response.accepted().build();
    }

    @GET
    @Path("/{id}")
    @Blocking //Para evitar error, ya que es reactivo pero la base de datos no lo es, por lo tanto es bloqueante
    public Response findById(@PathParam("id") Long id){
        Customer customer = repositoryCustomer.byId(id);
        return Response.ok(customer).build();
    }

    @GET
    @Path("/{id}/product")
    @Blocking //Para evitar error, ya que es reactivo pero la base de datos no lo es, por lo tanto es bloqueante
    public Uni<Customer> findByIdProducts(@PathParam("id") Long id){
        //¡Llamado a clase donde utilizo programación reactiva!
        return communicationServiceProduct.compareCustomerWhitProducts(id);
    }

    @DELETE
    @Path("/borrar/{id}")
    @Blocking //Para evitar error, ya que es reactivo pero la base de datos no lo es, por lo tanto es bloqueante
    public Response deleteCustomer(@PathParam("id") Long id) {
        repositoryCustomer.deleteCustomer(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/editar/{id}")
    @Blocking //Para evitar error, ya que es reactivo pero la base de datos no lo es, por lo tanto es bloqueante
    public Response editCustomer(@PathParam("id") Long id, Customer customer) {
        Customer customerUpdate = repositoryCustomer.editCustomer(id, customer);
        return Response.ok(customerUpdate).build();
    }


}