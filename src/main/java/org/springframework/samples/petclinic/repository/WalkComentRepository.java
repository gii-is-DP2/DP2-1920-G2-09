
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.WalkComent;

public interface WalkComentRepository extends CrudRepository<WalkComent, Integer> {

	@Query("Select wc from WalkComent wc where wc.walk.id = ?1 and wc.title != '' and wc.description != '' ")
	Collection<WalkComent> findComentsOfTheWalk(Integer idWalk);

	@Query("Select user from User user where username = ?1")
	User findUserByUsername(String username);

	@Query("SELECT AVG(wc.rating) FROM WalkComent wc where wc.walk.id =?1 and wc.rating is not null")
	Double getAverageRatingOfWalk(int walkId);
}
