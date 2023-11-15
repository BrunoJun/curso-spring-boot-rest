package org.me.cursoSpringBoot.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class WrapperPersonVO implements Serializable {

    private static final long serialVersionUID = 1l;

    @JsonProperty("_embedded")
    private PersonEmbededVO embedded;

    public WrapperPersonVO() {}

    public PersonEmbededVO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(PersonEmbededVO embedded) {
        this.embedded = embedded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WrapperPersonVO that = (WrapperPersonVO) o;

        return Objects.equals(embedded, that.embedded);
    }

    @Override
    public int hashCode() {
        return embedded != null ? embedded.hashCode() : 0;
    }
}
