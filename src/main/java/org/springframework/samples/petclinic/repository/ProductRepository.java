
package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

	@Query("Select p from Product p where LOWER(p.name) like LOWER(concat('%',?1,'%'))")
	Iterable<Product> findProductsFiltered(String name);

}
