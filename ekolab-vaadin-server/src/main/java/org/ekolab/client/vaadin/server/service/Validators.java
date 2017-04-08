package org.ekolab.client.vaadin.server.service;

import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by 777Al on 07.04.2017.
 */
@Service
@UIScope
public class Validators implements Serializable {

    @Autowired
    private I18N i18N;


    @Cacheable("validators-int")
    public IntegerRangeValidator intValidator(int min, int max) {
        return new IntegerRangeValidator(i18N.get("validator.must-be-number"), min, max);
    }

    @Cacheable("validators-double")
    public DoubleRangeValidator doubleValidator(double min, double max) {
        return new DoubleRangeValidator(i18N.get("validator.must-be-double"), min, max);
    }
}
