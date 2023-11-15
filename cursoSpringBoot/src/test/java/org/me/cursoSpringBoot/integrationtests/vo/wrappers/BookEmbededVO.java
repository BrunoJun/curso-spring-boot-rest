package org.me.cursoSpringBoot.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.me.cursoSpringBoot.integrationtests.vo.BookVO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BookEmbededVO implements Serializable {

    private static final long serialVersionUID = 1l;

    @JsonProperty("bookVOList")
    private List<BookVO> bookVOS;

    public BookEmbededVO() {}

    public List<BookVO> getBookVOS() {
        return bookVOS;
    }

    public void setBookVOS(List<BookVO> bookVOS) {
        this.bookVOS = bookVOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookEmbededVO that = (BookEmbededVO) o;

        return Objects.equals(bookVOS, that.bookVOS);
    }

    @Override
    public int hashCode() {
        return bookVOS != null ? bookVOS.hashCode() : 0;
    }
}
