package mk.ukim.finki.wp.exam.example.repository;

import java.util.List;
import mk.ukim.finki.wp.exam.example.model.Category;
import mk.ukim.finki.wp.exam.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByNameLike(String name);

    List<Product> findAllByCategoriesContains(Category category);
}
