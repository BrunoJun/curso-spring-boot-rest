package org.me.cursoSpringBoot.repositories;

import org.me.cursoSpringBoot.model.Book;
import org.me.cursoSpringBoot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
