package org.ekolab.server.service.api.content;

import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;

import javax.validation.Valid;

public interface LabExperimentService<T extends LabData<V>, V extends LabVariant> extends LabService<T, V> {
    V updateExperimentJournal(@Valid V experimentJournal);
}
