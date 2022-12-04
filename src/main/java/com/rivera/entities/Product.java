package com.rivera.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"customer", "product"}))
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer", referencedColumnName = "id")
  @JsonBackReference
  private Customer customer;

  //Este es id del producto, el de arriba es autogenerado
  @Column
  private Long product;

  //Con @Transient le indico que son atributos que no se guardan en la base de datos de este micro servicio
  @Transient
  private String code;
  @Transient
  private String name;
  @Transient
  private String description;

}
