package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.model.User;

public interface ProductComentRepository extends CrudRepository<ProductComent, Integer> {

    @Query("Select pc from ProductComent pc INNER JOIN Authorities a ON pc.user.username = a.username where a.authority='veterinarian' and pc.product.id = ?1 and pc.title != '' and pc.description != '' ")
    Collection<ProductComent> findComentsVetsOfTheProduct(Integer idProduct);
    
    @Query("Select pc from ProductComent pc INNER JOIN Authorities a ON pc.user.username = a.username where a.authority='owner' and pc.product.id = ?1 and pc.title != '' and pc.description != '' ")
    Collection<ProductComent> findComentsOwnersOfTheProduct(Integer idProduct);

    @Query("Select user from User user where username = ?1")
    User findUserByUsername(String username);

    
    @Query("SELECT AVG(pc.rating) FROM ProductComent pc where pc.product.id =?1 and pc.rating is not null")
    Double getAverageRatingOfProduct(int ProductId);

}
