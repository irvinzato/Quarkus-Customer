package com.rivera.repositories;

import com.rivera.entities.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class RepositoryCustomer {

  @Inject
  EntityManager entityManager;

  @Transactional
  public void createCustomer(Customer customer){
    entityManager.persist(customer);
  }

  @Transactional
  public void deleteCustomer(Long id) {
    Customer customer = byId(id);
    entityManager.remove(customer);
  }

  @Transactional
  public List<Customer> listCustomer(){
    List<Customer> customers = entityManager.createQuery("SELECT c FROM Customer c").getResultList();
    return customers;
  }

  @Transactional
  public void editCustomer(Long id, Customer customer){
    Customer findCustomer = (Customer) entityManager.createQuery("SELECT c FROM Customer c WHERE c.id=:idParam")
            .setParameter("idParam", id)
            .getSingleResult();
    findCustomer.setCode(customer.getCode());
    findCustomer.setPhone(customer.getPhone());
    findCustomer.setNames(customer.getNames());
    findCustomer.setAddress(customer.getAddress());
    findCustomer.setSurname(customer.getSurname());
    findCustomer.setAccountNumber(customer.getAccountNumber());
  }

  @Transactional
  public Customer byId(Long id){
    Customer customer = (Customer) entityManager.createQuery("SELECT c FROM Customer c WHERE c.id=:idParam")
            .setParameter("idParam", id)
            .getSingleResult();
    return customer;
  }

}
