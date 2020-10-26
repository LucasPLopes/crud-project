package br.com.challenge.api.repository;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

import br.com.challenge.api.domain.Product;
import br.com.challenge.api.domain.QProduct;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , 
QuerydslPredicateExecutor<Product>, QuerydslBinderCustomizer<QProduct> {
    @Override
    default public void customize(
      QuerydslBindings bindings, QProduct root) {
        bindings.bind(String.class)
          .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
      }
    
}