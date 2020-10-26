package br.com.challenge.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.challenge.api.domain.Product;
import br.com.challenge.api.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product create(Product product){
        product.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return this.productRepository.save(product);
    }

	public List<Product> findAll() {
		return this.productRepository.findAll();
	}
    
    public void delete(Long id ){
        this.productRepository.deleteById(id);
    }

	public Product findById(Long id) {
        return this.productRepository.findById(id).get();    
    }
    
    public Product update(Product product){
        Optional<Product> finded = this.productRepository.findById(product.getId());
        if(finded.isPresent()){
            Product updated = finded.get();

            updated.setDescription(product.getDescription());
            updated.setImage(product.getImage());
            updated.setTags(product.getTags());
            
            return  this.productRepository.save(updated);
        }
        return null;
    }
}
