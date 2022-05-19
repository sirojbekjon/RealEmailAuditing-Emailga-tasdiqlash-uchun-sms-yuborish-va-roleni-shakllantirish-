package com.example.appjwtrealemailauditing.repository;

import com.example.appjwtrealemailauditing.entity.Produkt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "product")
public interface ProductRepository extends JpaRepository<Produkt, Integer> {

}
