package org.me.cursoSpringBoot.integrationtests.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.me.cursoSpringBoot.integrationtests.testcontainers.AbstractIntegrationTest;
import org.me.cursoSpringBoot.model.Person;
import org.me.cursoSpringBoot.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    public PersonRepository personRepository;

    private static Person person;

    @BeforeAll
    public static void setup(){

        person = new Person();
    }

    @Test
    @Order(0)
    void testFindByName() throws JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));

        person = personRepository.findPersonsByName("br", pageable).getContent().get(0);

        assertEquals(331, person.getId());
        assertEquals("Albrecht", person.getFirstName());
        assertEquals("Tassell", person.getLastName());
        assertEquals("638 Northwestern Plaza", person.getAddress());
        assertEquals("Male", person.getGender());
    }

    @Test
    @Order(1)
    void testDiablePerson() throws JsonProcessingException {

        personRepository.disablePerson(person.getId());

        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));

        person = personRepository.findPersonsByName("br", pageable).getContent().get(0);

        assertEquals(331, person.getId());
        assertEquals("Albrecht", person.getFirstName());
        assertEquals("Tassell", person.getLastName());
        assertEquals("638 Northwestern Plaza", person.getAddress());
        assertEquals("Male", person.getGender());

        assertFalse(person.getEnabled());
    }
}
