package org.ekolab.server.model;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class StudentGroup extends IdentifiedDomainModel {
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentGroup)) return false;
        StudentGroup group = (StudentGroup) o;
        return Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
