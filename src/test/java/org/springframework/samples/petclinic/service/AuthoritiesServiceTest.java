package org.springframework.samples.petclinic.service;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AuthoritiesServiceTest {

	@Autowired
	private AuthoritiesService authoritiesService;

	@Test
	void shoudlFindAllAuthorities() {
		Assert.assertTrue(this.authoritiesService.getAllAuthorities().size() > 0);
	}

	@Test
	void shouldSaveAuthority() {
		List<Authorities> ls = this.authoritiesService.getAllAuthorities();
		Integer before = ls.size();
		Authorities au = new Authorities();
		au.setAuthority("nuevaAuth");
		au.setUsername("userNuevo");
		this.authoritiesService.saveAuthorities(au);
		List<Authorities> newAllAuth = this.authoritiesService.getAllAuthorities();
		Integer after = newAllAuth.size();
		Assert.assertTrue(after == before + 1);
	}

	@Test
	void shouldSaveAuthorityGivingUsernameAndAuthority() {
		List<Authorities> ls = this.authoritiesService.getAllAuthorities();
		Integer before = ls.size();
		this.authoritiesService.saveAuthorities("userNuevo", "NuevaAuth");
		List<Authorities> newAllAuth = this.authoritiesService.getAllAuthorities();
		Integer after = newAllAuth.size();
		Assert.assertTrue(after == before + 1);
	}

}
