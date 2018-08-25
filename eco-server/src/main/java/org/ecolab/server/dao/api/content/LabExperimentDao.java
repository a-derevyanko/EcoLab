package org.ecolab.server.dao.api.content;

import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;

public interface LabExperimentDao<T extends LabData<V>, V extends LabVariant> extends LabDao<T> {
    void updateExperimentJournal(V experimentJournal);
}
