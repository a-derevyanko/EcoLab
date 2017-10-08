package org.ekolab.server.model;

import java.util.Objects;

/**
 * Created by 777Al on 22.05.2017.
 */
public class StudentTeam implements DomainModel {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentTeam)) return false;
        StudentTeam that = (StudentTeam) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
