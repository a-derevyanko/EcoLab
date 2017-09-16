package org.ekolab.client.vaadin.server.service;

import com.github.lotsabackscatter.blueimp.gallery.Image;
import com.github.lotsabackscatter.blueimp.gallery.Options;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PresentationService {
    @NotEmpty
    @NotNull
    List<Image> getPresentationSlides(int labNumber);


    @NotNull
    Options getPresentationOptions();
}
