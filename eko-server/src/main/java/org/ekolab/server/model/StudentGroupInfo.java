package org.ekolab.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentGroupInfo extends IdentifiedDomainModel {
    private int index;
    private String name;
    private int studentCount;
    private int labCount;
    private int defenceCount;
}
