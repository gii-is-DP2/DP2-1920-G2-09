
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

	private ProductRepository productRepository;

	@Autowired
	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public Iterable<Product> findAllProducts() {
		return this.productRepository.findAll();
	}

	@Transactional
	public Iterable<Product> findFilteredProducts(final String name) {
		return this.productRepository.findProductsFiltered(name);
	}

	@Transactional
	public Iterable<Product> findFilteredProductsByCategory(final Category category) {
		return this.productRepository.findProductsFilteredByCategory(category);
	}

	@Transactional(readOnly = true)
	public Product findProductById(final int id) {
		return this.productRepository.findById(id);
	}

	@Transactional
	public void saveProduct(final Product product) {
		if (product.getStock() == 0) {
			product.setAvailable(false);
		}
		this.productRepository.save(product);
	}

}
