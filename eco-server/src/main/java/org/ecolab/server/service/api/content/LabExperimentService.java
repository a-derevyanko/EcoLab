package org.ecolab.server.service.api.content;

import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;

import javax.validation.Valid;

public interface LabExperimentService<T extends LabData<V>, V extends LabVariant> extends LabService<T, V> {
    V updateExperimentJournal(@Valid V experimentJournal);
}
