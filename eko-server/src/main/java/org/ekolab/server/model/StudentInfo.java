package org.ekolab.server.model;

import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by 777Al on 22.05.2017.
 */
public class StudentInfo implements DomainModel {
    @Size(max = 256)
    private String team;

    @Size(max = 256)
    private String group;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentInfo that = (StudentInfo) o;
        return Objects.equals(team, that.team) &&
                Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, group);
    }
}
