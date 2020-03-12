package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.model.User;

public interface ProductComentRepository extends CrudRepository<ProductComent, Integer> {

    @Query("Select pc from ProductComent pc where pc.product.id = ?1")
    Collection<ProductComent> findComentsOfTheProduct(Integer idProduct);

    @Query("Select user from User user where username = ?1")
    User findUserByUsername(String username);

}
