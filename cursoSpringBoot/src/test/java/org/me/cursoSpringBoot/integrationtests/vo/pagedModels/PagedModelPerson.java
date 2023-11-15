package org.me.cursoSpringBoot.integrationtests.vo.pagedModels;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.me.cursoSpringBoot.integrationtests.vo.PersonVO;

import java.util.List;

@XmlRootElement
public class PagedModelPerson {

    @XmlElement(name = "content")
    private List<PersonVO> content;

    public PagedModelPerson() {}

    public List<PersonVO> getContent() {
        return content;
    }

    public void setContents(List<PersonVO> content) {
        this.content = content;
    }
}
