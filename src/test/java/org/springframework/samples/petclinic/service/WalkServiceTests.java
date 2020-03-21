
package org.springframework.samples.petclinic.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.stereotype.Service;

@DisplayName("Walks Service Tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class WalkServiceTests {

    @Autowired
    protected WalkService walkService;

    @Test
    void shouldFindWalks() {
    	Iterable<Walk> walks = this.walkService.findAllWalks();
    	Assertions.assertTrue(walks !=null);
    }

    @Test
    void shouldFindWalkById() {
    	Walk walk = this.walkService.findWalkById(1);
    	Assertions.assertTrue(walk != null);
    }

    @Test
    void shouldNotFindWalkById() {
    	Walk walk = this.walkService.findWalkById(-1);
    	Assertions.assertTrue(walk == null);
    }
    
    @Test
    void shouldFindAvailableWalks() {
    	Iterable<Walk> walks = this.walkService.findAllWalks();
    	Assertions.assertTrue(walks != null);
    }
    
    @Test
    void shouldSaveWalks() {
    	Walk walk = new Walk();
    	walk.setName("Paseo");
    	walk.setDescription("This is a try description");
    	walk.setMap("https://tinyurl.com/wygb5vu");
    	this.walkService.saveWalk(walk);
    	Assertions.assertTrue(walk != null);
    }

}
