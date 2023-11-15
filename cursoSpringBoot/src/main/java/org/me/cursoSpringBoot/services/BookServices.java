package org.me.cursoSpringBoot.services;

import org.me.cursoSpringBoot.controllers.BookController;
import org.me.cursoSpringBoot.controllers.PersonController;
import org.me.cursoSpringBoot.data.vo.v1.BookVO;
import org.me.cursoSpringBoot.data.vo.v1.PersonVO;
import org.me.cursoSpringBoot.exceptions.RequiredObjectIsNullException;
import org.me.cursoSpringBoot.exceptions.ResourceNotFoundException;
import org.me.cursoSpringBoot.mapper.DozerMapper;
import org.me.cursoSpringBoot.model.Book;
import org.me.cursoSpringBoot.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    @Autowired
    BookRepository repository;

    @Autowired
    PagedResourcesAssembler<BookVO> assembler;

    private Logger logger = Logger.getLogger(BookServices.class.getName());

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) throws Exception {

        logger.info("Finding all books...");

        var bookPage = repository.findAll(pageable);
        var bookVOPages = bookPage.map(p -> DozerMapper.parseObject(p, BookVO.class));
        bookVOPages.map(p -> {
            try {
                return p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(bookVOPages, link);
    }

    public BookVO findById(Long id) throws Exception {

        logger.info("Finding one book...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found!"));

        BookVO bookVO = DozerMapper.parseObject(entity, BookVO.class);

        bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());

        return bookVO;
    }

    public BookVO create(BookVO book) throws Exception {

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one book...");

        var entity = DozerMapper.parseObject(book, Book.class);

        var bookVO = DozerMapper.parseObject(repository.save(entity), BookVO.class);

        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());

        return bookVO;
    }

    public BookVO update(BookVO book) throws Exception {

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one book...");

        var entity = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found!"));

        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());

        var bookVO = DozerMapper.parseObject(repository.save(entity), BookVO.class);

        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());

        return bookVO;
    }

    public void delete(Long id){

        logger.info("Deleting one book...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found!"));

        repository.delete(entity);
    }
}
