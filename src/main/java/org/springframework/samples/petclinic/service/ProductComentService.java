package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.ProductComentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductComentService {

    ProductComentRepository productComentRepository;

    @Autowired
    public ProductComentService(final ProductComentRepository productComentRepository) {
	this.productComentRepository = productComentRepository;
    }

    @Transactional
    public Collection<ProductComent> findAllComentsOfTheProduct(final Integer idProduct) {
	return this.productComentRepository.findComentsOfTheProduct(idProduct);
    }

    @Transactional
    public User findUserByUsername(final String username) {
	return this.productComentRepository.findUserByUsername(username);
    }

    @Transactional
    public void saveProductComent(final ProductComent pc) {
	this.productComentRepository.save(pc);
    }

}
