package org.ekolab.server.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class LabData {
    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime saveDate;
}
