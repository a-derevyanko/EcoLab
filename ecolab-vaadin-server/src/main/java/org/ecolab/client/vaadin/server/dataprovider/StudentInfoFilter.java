package org.ecolab.client.vaadin.server.dataprovider;

import org.ecolab.server.model.StudentInfo;

import java.util.Objects;

public class StudentInfoFilter extends UserInfoFilter {
    private StudentInfo studentInfoFilter = new StudentInfo();

    public StudentInfo getStudentInfoFilter() {
        return studentInfoFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentInfoFilter)) return false;
        if (!super.equals(o)) return false;
        StudentInfoFilter that = (StudentInfoFilter) o;
        return Objects.equals(studentInfoFilter, that.studentInfoFilter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentInfoFilter);
    }
}
