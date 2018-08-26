package org.ecolab.server.model;

import java.util.Objects;

/**
 * Created by 777Al on 22.05.2017.
 */
public class StudentTeam extends IdentifiedDomainModel {
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
        if (!(o instanceof StudentTeam)) return false;
        StudentTeam that = (StudentTeam) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
