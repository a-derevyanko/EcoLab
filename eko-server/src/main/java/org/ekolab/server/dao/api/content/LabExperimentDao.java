package org.ekolab.server.dao.api.content;

import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;

public interface LabExperimentDao<T extends LabData<V>, V extends LabVariant> extends LabDao<T> {
    void updateExperimentJournal(V experimentJournal);
}
