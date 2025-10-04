package com.ecommerce.catalogueservice.repository;

import com.ecommerce.catalogueservice.entity.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {
}


