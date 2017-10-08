package org.ekolab.server.model;

import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by 777Al on 22.05.2017.
 */
public class StudentInfo implements DomainModel {
    private StudentTeam team;

    @Size(max = 256)
    private String group;

    @Size(max = 256)
    private String teacher;

    public StudentTeam getTeam() {
        return team;
    }

    public void setTeam(StudentTeam team) {
        this.team = team;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentInfo)) return false;
        StudentInfo that = (StudentInfo) o;
        return Objects.equals(team, that.team) &&
                Objects.equals(group, that.group) &&
                Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, group, teacher);
    }
}
