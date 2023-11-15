package org.me.cursoSpringBoot.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.me.cursoSpringBoot.integrationtests.vo.PersonVO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PersonEmbededVO implements Serializable {

    private static final long serialVersionUID = 1l;

    @JsonProperty("personVOList")
    private List<PersonVO> personVOS;

    public PersonEmbededVO() {}

    public List<PersonVO> getPersonVOS() {
        return personVOS;
    }

    public void setPersonVOS(List<PersonVO> personVOS) {
        this.personVOS = personVOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonEmbededVO that = (PersonEmbededVO) o;

        return Objects.equals(personVOS, that.personVOS);
    }

    @Override
    public int hashCode() {
        return personVOS != null ? personVOS.hashCode() : 0;
    }
}
