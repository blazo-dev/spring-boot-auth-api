package dev.blazo.crud.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.blazo.crud.entity.Product;
import dev.blazo.crud.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public void save(Product product) {
        productRepository.save(product);
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Optional<Product> getOne(int id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public boolean existsById(int id) {
        return productRepository.existsById(id);
    }

}
