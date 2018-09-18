package org.ecolab.server.model.content;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ecolab.server.model.IdentifiedDomainModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by 777Al on 19.04.2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class LabData<V extends LabVariant> extends IdentifiedDomainModel {
    @NotNull
    private V variant;

    @NotNull
    private Set<Long> users;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime saveDate;

    @NotNull
    private boolean completed;
}
