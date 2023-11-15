package org.me.cursoSpringBoot.unitTests.mapper.mocks;

import org.me.cursoSpringBoot.data.vo.v1.BookVO;
import org.me.cursoSpringBoot.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MockBook {
    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number);
        book.setTitle("Title Test" + number);
        book.setAuthor("Author Name Test" + number);

        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO bookVO = new BookVO();
        bookVO.setTitle("Title Test" + number);
        bookVO.setAuthor("Author Name Test" + number);

        return bookVO;
    }

}
