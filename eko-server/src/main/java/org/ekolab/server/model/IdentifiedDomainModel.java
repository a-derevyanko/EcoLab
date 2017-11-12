package org.ekolab.server.model;

/**
 * Created by Андрей on 15.05.2017.
 */
public class IdentifiedDomainModel implements DomainModel {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
