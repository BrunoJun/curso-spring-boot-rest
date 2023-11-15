package org.me.cursoSpringBoot.integrationtests.vo.pagedModels;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.me.cursoSpringBoot.integrationtests.vo.BookVO;
import org.me.cursoSpringBoot.integrationtests.vo.PersonVO;

import java.util.List;

@XmlRootElement
public class PagedModelBook {

    @XmlElement(name = "content")
    private List<BookVO> content;

    public PagedModelBook() {}

    public List<BookVO> getContent() {
        return content;
    }

    public void setContent(List<BookVO> content) {
        this.content = content;
    }
}
