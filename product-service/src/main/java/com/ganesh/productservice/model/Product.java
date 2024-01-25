package com.ganesh.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product") //@Document is not a standard Java annotation, but it is commonly used in the context of object-relational mapping (ORM) frameworks, such as Spring Data MongoDB or Spring Data Elasticsearch.Also used in document based Database;
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder//@Builder is an annotation from the Project Lombok library, which is used to automatically generate builder methods for a class. Builder pattern is a creational design pattern that provides a way to construct a complex object step by step.
public class Product {
    @Id
    private String id;
    private String name;
    private String description;

    private BigDecimal price; //BigDecimal is used to represent numbers with arbitrary precision, especially in situations where the precision of float and double may not be sufficient or may lead to rounding errors.
}
