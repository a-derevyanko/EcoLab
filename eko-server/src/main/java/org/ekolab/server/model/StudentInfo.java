package org.ekolab.server.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StudentInfo implements DomainModel {
    private StudentTeam team;

    @NotNull
    private StudentGroup group;

    public StudentTeam getTeam() {
        return team;
    }

    public void setTeam(StudentTeam team) {
        this.team = team;
    }

    public StudentGroup getGroup() {
        return group;
    }

    public void setGroup(StudentGroup group) {
        this.group = group;
    }
}
