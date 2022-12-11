package com.rivera.comunicationotherservices;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.ArrayList;
import javax.inject.Inject;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import com.rivera.entities.Customer;
import com.rivera.entities.Product;
import com.rivera.repositories.RepositoryCustomer;

//Esta clase utiliza librerías del POM, "quarkus-vertx" y "smallrye-mutiny-vertx-web-client"
//Para programación reactiva
@Slf4j
@Singleton
public class CommunicationServiceProduct {

  @Inject
  Vertx vertx;

  @Inject
  private RepositoryCustomer repositoryCustomer;

  private WebClient webClient;

  //Metodo que se inicializa después de haber construido la clase, utilizado para consumir otra ruta
  @PostConstruct
  void initialize(){
    this.webClient = WebClient.create(vertx,
            new WebClientOptions().setDefaultHost("localhost")
                    .setDefaultPort(8081).setSsl(false).setTrustAll(true));
  }

  //Método para recuperar cliente de manera reactiva
  private Uni<Customer> getCustomerReactive(Long id){
    Customer customer = repositoryCustomer.byId(id);
    Uni<Customer> uniCustomer = Uni.createFrom().item(customer);
    return uniCustomer;
  }

  //Método para recuperar productos de otro micro servicio, forma reactiva con caso de éxito y error
  //La idea es obtener todos los productos, posteriormente los filtraré con los que debe tener el cliente
  private Uni<List<Product>> getAllProduct(){
    return webClient.get(8081, "localhost", "/product").send()
            .onFailure().invoke(res -> log.error("Error al cargar productos del otro ms", res))
            .onItem().transform(res -> {
              List<Product> listProducts = new ArrayList<>();
              JsonArray objects = res.bodyAsJsonArray();
              objects.forEach(p -> {
                log.info("Objetos del body " + objects);
                ObjectMapper objectMapper = new ObjectMapper();
                //Pasar JSON string a POJO class
                Product product = null;
                try {
                  product = objectMapper.readValue(p.toString(), Product.class);
                }catch(JsonProcessingException exception){
                  exception.printStackTrace();
                }
                listProducts.add(product);
              });
              return listProducts;
            });
  }

  public Uni<Customer> compareCustomerWhitProducts(Long id){
    return Uni.combine().all().unis(getCustomerReactive(id), getAllProduct())
            .combinedWith((customer, products) -> {
              customer.getProducts().forEach(prod -> {
                products.forEach(p -> {
                  if(prod.getProduct().equals(p.getId())) {
                    prod.setName(p.getName());
                    prod.setDescription(p.getDescription());
                  }
                });
              });
              return customer;
            });
  }

}
