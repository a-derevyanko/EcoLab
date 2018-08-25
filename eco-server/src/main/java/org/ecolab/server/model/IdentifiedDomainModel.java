package org.ecolab.server.model;

import lombok.Data;

/**
 * Created by Андрей on 15.05.2017.
 */
@Data
public class IdentifiedDomainModel implements DomainModel {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
