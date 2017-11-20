package org.ekolab.client.vaadin.server.service.api;

import com.github.lotsabackscatter.blueimp.gallery.Image;
import com.github.lotsabackscatter.blueimp.gallery.Options;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public interface PresentationService extends Serializable {
    @Size(min = 1)
    @NotNull
    List<Image> getPresentationSlides(int labNumber);


    @NotNull
    Options getPresentationOptions();
}
