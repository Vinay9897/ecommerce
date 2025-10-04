package com.ecommerce.subcatalogueservice.repository;

import com.ecommerce.subcatalogueservice.entity.Subcatalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcatalogueRepository extends JpaRepository<Subcatalogue, Long> {
}


