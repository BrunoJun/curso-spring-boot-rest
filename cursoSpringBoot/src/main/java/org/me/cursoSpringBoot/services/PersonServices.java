package org.me.cursoSpringBoot.services;

import jakarta.transaction.Transactional;
import org.me.cursoSpringBoot.controllers.PersonController;
import org.me.cursoSpringBoot.data.vo.v1.PersonVO;
import org.me.cursoSpringBoot.data.vo.v2.PersonVOV2;
import org.me.cursoSpringBoot.exceptions.RequiredObjectIsNullException;
import org.me.cursoSpringBoot.exceptions.ResourceNotFoundException;
import org.me.cursoSpringBoot.mapper.DozerMapper;
import org.me.cursoSpringBoot.mapper.custom.PersonMapper;
import org.me.cursoSpringBoot.model.Person;
import org.me.cursoSpringBoot.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) throws Exception {

        logger.info("Finding all people...");

        var personPage = repository.findAll(pageable);
        var personVOPages = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
        personVOPages.map(p -> {
            try {
                return p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(personVOPages, link);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName ,Pageable pageable) throws Exception {

        logger.info("Finding person(s) with " + firstName +"...");

        var personPage = repository.findPersonsByName(firstName, pageable);
        var personVOPages = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
        personVOPages.map(p -> {
            try {
                return p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(personVOPages, link);
    }

    public PersonVO findById(Long id) throws Exception {

        logger.info("Finding one person...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));

        PersonVO personVO = DozerMapper.parseObject(entity, PersonVO.class);

        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

        return personVO;
    }

    public PersonVO create(PersonVO person) throws Exception {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person...");

        var entity = DozerMapper.parseObject(person, Person.class);

        var personVO = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());

        return personVO;
    }

    public PersonVOV2 createV2(PersonVOV2 personVOV2) throws Exception {

        if (personVOV2 == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person...");

        var entity = personMapper.convertVOToEntity(personVOV2);

        var personVO = personMapper.convertEntityToVO(repository.save(entity));

        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());

        return personVO;
    }

    public PersonVO update(PersonVO person) throws Exception {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one person...");

        var entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var personVO = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());

        return personVO;
    }

    @Transactional
    public PersonVO disablePerson(Long id) throws Exception {

        logger.info("Disabling one person...");

        repository.disablePerson(id);

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));

        PersonVO personVO = DozerMapper.parseObject(entity, PersonVO.class);

        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

        return personVO;
    }

    public void delete(Long id){

        logger.info("Deleting one person...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));

        repository.delete(entity);
    }
}
