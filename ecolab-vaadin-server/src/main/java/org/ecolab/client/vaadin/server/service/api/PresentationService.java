package org.ecolab.client.vaadin.server.service.api;

import com.github.lotsabackscatter.blueimp.gallery.Image;
import com.github.lotsabackscatter.blueimp.gallery.Options;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public interface PresentationService extends Serializable {
    @NotEmpty
    @NotNull
    List<Image> getPresentationSlides(int labNumber);


    @NotNull
    Options getPresentationOptions();
}
