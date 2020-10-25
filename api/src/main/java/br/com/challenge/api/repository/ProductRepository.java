package br.com.challenge.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import br.com.challenge.api.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}