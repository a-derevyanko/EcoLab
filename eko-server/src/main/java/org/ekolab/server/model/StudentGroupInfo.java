package org.ekolab.server.model;

import lombok.Data;

@Data
public class StudentGroupInfo implements DomainModel {
    private int index;
    private String name;
    private int studentCount;
    private int labCount;
    private int defenceCount;
}
