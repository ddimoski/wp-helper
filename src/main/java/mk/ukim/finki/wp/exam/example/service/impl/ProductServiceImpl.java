package mk.ukim.finki.wp.exam.example.service.impl;

import java.util.List;
import java.util.stream.*;
import mk.ukim.finki.wp.exam.example.model.Category;
import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidCategoryIdException;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidProductIdException;
import mk.ukim.finki.wp.exam.example.repository.CategoryRepository;
import mk.ukim.finki.wp.exam.example.repository.ProductRepository;
import mk.ukim.finki.wp.exam.example.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> listAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
    }

    @Override
    public Product create(String name, Double price, Integer quantity, List<Long> categoryIds) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        Product product = new Product(name, price, quantity, categories);
        return this.productRepository.save(product);
    }

    @Override
    public Product update(Long id, String name, Double price, Integer quantity, List<Long> categoryIds) {
        Product product = this.findById(id);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);

        List<Category> categories = categoryRepository.findAllById(categoryIds);
        product.setCategories(categories);
        return this.productRepository.save(product);
    }

    @Override
    public Product delete(Long id) {
        Product product = this.findById(id);
        productRepository.delete(product);
        return product;
    }

    @Override
    public List<Product> listProductsByNameAndCategory(String name, Long categoryId) {
        List<Product> filteredProducts;
        if(name != null && categoryId != null) {
            List<Product> products = this.productRepository.findAllByNameLike(name);
            Category category = categoryRepository.findById(categoryId).orElseThrow(InvalidCategoryIdException::new);

            filteredProducts = products.stream()
                .filter(x -> x.getCategories().contains(category)).collect(Collectors.toList());
        }
        else if (name == null && categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(InvalidCategoryIdException::new);
            filteredProducts = this.productRepository.findAllByCategoriesContains(category);
        }
        else if (name != null){
            filteredProducts = this.productRepository.findAllByNameLike(name);
        }
        else {
            filteredProducts = listAllProducts();
        }

        return filteredProducts;
    }
}
