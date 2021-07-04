package br.com.produto.repository.product.helper;

import br.com.produto.domain.product.Product;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProducts(String q, BigDecimal minPrice, BigDecimal maxPrice) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);

        Root<Product> root = criteria.from(Product.class);

        List<Predicate> predicates = createPredicates(q, minPrice, maxPrice, builder, root);

        criteria.where(predicates.toArray(new Predicate[predicates.size()]));

        return manager.createQuery(criteria).getResultList();

    }

    private List<Predicate> createPredicates(String q, BigDecimal minPrice, BigDecimal maxPrice,
                                             CriteriaBuilder builder, Root<Product> root) {

        List<Predicate> predicates = new ArrayList<>();

        if (q != null && !q.trim().isEmpty()) {

            Predicate name = builder.like(
                    builder.lower(root.get("name")),
                    "%" + q.toLowerCase() + "%");

            Predicate description = builder.like(
                    builder.lower(root.get("description")),
                    "%" + q.toLowerCase() + "%");

            predicates.add(builder.or(name, description));
        }

        if (minPrice != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        return predicates;
    }
}
