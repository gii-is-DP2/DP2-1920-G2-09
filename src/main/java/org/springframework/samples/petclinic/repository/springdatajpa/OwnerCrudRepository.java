package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Owner;

public interface OwnerCrudRepository extends CrudRepository<Owner, Integer> {

    @Query("Select owner from Owner owner where owner.user.username = ?1")
    Owner findOwnerByUsername(String username);

    @Query("Select count(user) from User user where username = ?1")
    Integer countOwnersWithSameUserName(String username);

}
