package org.ekolab.server.service.api.content.lab3;

import org.ekolab.server.model.content.lab3.City;
import org.ekolab.server.model.content.lab3.WindDirection;
import org.ekolab.server.service.api.content.LabResourceService;

import java.awt.*;
import java.net.URL;

/**
 * Created by 777Al on 24.04.2017.
 */
public interface Lab3ResourceService extends LabResourceService{
    URL getWindRose(City city);

    Image getWindRoseImage(City city);

    Image getBackgroundImage(City city, WindDirection windDirection);
}
