package org.springframework.samples.petclinic.service;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthoritiesServiceTest {

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
		Assertions.assertEquals(after, before + 1);
	}

	@Test
	void shouldSaveAuthorityGivingUsernameAndAuthority() {
		List<Authorities> ls = this.authoritiesService.getAllAuthorities();
		Integer before = ls.size();
		this.authoritiesService.saveAuthorities("userNuevo", "NuevaAuth");
		List<Authorities> newAllAuth = this.authoritiesService.getAllAuthorities();
		Integer after = newAllAuth.size();
		Assertions.assertEquals(after, before + 1);
	}
// PRUEBAS PARAMETRIZADAS

	@ParameterizedTest
	@CsvSource({ "AUTORIDAD1,         USER1", "au.th2,         us.er2", "AUth3auTH3,         UsEr3" })
	void shouldSaveAuthoritiesParametrized(final String auth, final String user) {
		List<Authorities> ls = this.authoritiesService.getAllAuthorities();
		Integer before = ls.size();
		Authorities au = new Authorities();
		au.setAuthority(auth);
		au.setUsername(user);
		this.authoritiesService.saveAuthorities(au);
		List<Authorities> newAllAuth = this.authoritiesService.getAllAuthorities();
		Integer after = newAllAuth.size();
		Assertions.assertEquals(after, before + 1);
	}

}
