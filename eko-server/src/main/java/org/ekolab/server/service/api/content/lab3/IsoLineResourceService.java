package org.ekolab.server.service.api.content.lab3;

import org.ekolab.server.model.content.lab3.City;
import org.ekolab.server.model.content.lab3.WindDirection;

import java.awt.*;

/**
 * Created by 777Al on 24.04.2017.
 */
public interface IsoLineResourceService {
    Image getWindRose(City city);

    Image getBackground(City city, WindDirection windDirection);
}
