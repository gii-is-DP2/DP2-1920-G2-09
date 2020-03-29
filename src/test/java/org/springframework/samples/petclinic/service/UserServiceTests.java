package org.springframework.samples.petclinic.service;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class UserServiceTests {

	@Autowired
	private UserService userService;

	@Test
	void shoudlFindAllAuthorities() {
		Assert.assertTrue(this.userService.findAllUsers().size() > 0);
	}

	@Test
	void shouldSaveUser() {
		List<User> ls = this.userService.findAllUsers();
		Integer before = ls.size();
		User u = new User();
		u.setUsername("nuevoUser");
		u.setEnabled(true);
		u.setEmail("user@gmail.com");
		u.setPassword("111222333");
		this.userService.saveUser(u);
		List<User> newAlluser = this.userService.findAllUsers();
		Integer after = newAlluser.size();
		Assert.assertTrue(after == before + 1);
	}

	// PRUEBAS PARAMETRIZADAS
	@ParameterizedTest
	@CsvSource({ "usuario1,email@gmail1.com,passsssword1", "usuario2,NuEvO@gmail2.com,passsssword2",
			"usuario1,NUEVOEMAIL13@gmail3.com,passsssword3" })
	void shouldSaveUserParametrized(final String username, final String email, final String password) {
		List<User> ls = this.userService.findAllUsers();
		Integer before = ls.size();
		User u = new User();
		u.setUsername(username);
		u.setEnabled(true);
		u.setEmail(email);
		u.setPassword(password);
		this.userService.saveUser(u);
		List<User> newAlluser = this.userService.findAllUsers();
		Integer after = newAlluser.size();
		Assert.assertTrue(after == before + 1);
	}

}
