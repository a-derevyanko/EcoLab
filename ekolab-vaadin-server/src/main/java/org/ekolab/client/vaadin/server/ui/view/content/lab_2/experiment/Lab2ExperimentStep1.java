package org.ekolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabExperimentJournalStep;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.security.util.FieldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2ExperimentStep1 extends LabExperimentJournalStep<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> {

    // ----------------------------- Графические компоненты --------------------------------
    private final Grid<Data> averageSoundPressureControlPointGrid = new Grid<>();

    public Lab2ExperimentStep1(Binder<Lab2ExperimentLog> experimentLogBinder,
                               Binder<Lab2Data<Lab2ExperimentLog>> dataBinder,
                               I18N i18N,
                               ValidationService validationService,
                               ParameterCustomizer parameterCustomizer) {
        super(experimentLogBinder, dataBinder, i18N, validationService, parameterCustomizer);
    }

    @Override
    public void init() {
        super.init();
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "name"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "barometricPressure"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "indoorsTemperature"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "roomSize"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "quantityOfSingleTypeEquipment"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "hemisphereRadius"));

        secondLayout.addComponent(averageSoundPressureControlPointGrid);
        averageSoundPressureControlPointGrid.setSizeFull();

        averageSoundPressureControlPointGrid.addColumn(Data::getNumber).setCaption(i18N.get("lab2.step1.random-data.point-number"));
        averageSoundPressureControlPointGrid.addColumn(Data::getX1).setCaption("31,5");
        averageSoundPressureControlPointGrid.addColumn(Data::getX2).setCaption("63");
        averageSoundPressureControlPointGrid.addColumn(Data::getX3).setCaption("125");
        averageSoundPressureControlPointGrid.addColumn(Data::getX4).setCaption("250");
        averageSoundPressureControlPointGrid.addColumn(Data::getX5).setCaption("500");
        averageSoundPressureControlPointGrid.addColumn(Data::getX6).setCaption("1000");
        averageSoundPressureControlPointGrid.addColumn(Data::getX7).setCaption("2000");
        averageSoundPressureControlPointGrid.addColumn(Data::getX8).setCaption("4000");
        averageSoundPressureControlPointGrid.addColumn(Data::getX9).setCaption("8000");
        fillAverageSoundPressureControlPointGrid(4);
    }

    private void fillAverageSoundPressureControlPointGrid(int rows) {
        List<Data> items = new ArrayList<>();
        IntStream.rangeClosed(1, rows).forEach(i -> {
            Data data = new Data();
            data.setNumber(i);
            items.add(data);
        });
        averageSoundPressureControlPointGrid.setItems(items);
    }

    private class Data {
        int number;
        double x1;
        double x2;
        double x3;
        double x4;
        double x5;
        double x6;
        double x7;
        double x8;
        double x9;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public double getX1() {
            return x1;
        }

        public void setX1(double x1) {
            this.x1 = x1;
        }

        public double getX2() {
            return x2;
        }

        public void setX2(double x2) {
            this.x2 = x2;
        }

        public double getX3() {
            return x3;
        }

        public void setX3(double x3) {
            this.x3 = x3;
        }

        public double getX4() {
            return x4;
        }

        public void setX4(double x4) {
            this.x4 = x4;
        }

        public double getX5() {
            return x5;
        }

        public void setX5(double x5) {
            this.x5 = x5;
        }

        public double getX6() {
            return x6;
        }

        public void setX6(double x6) {
            this.x6 = x6;
        }

        public double getX7() {
            return x7;
        }

        public void setX7(double x7) {
            this.x7 = x7;
        }

        public double getX8() {
            return x8;
        }

        public void setX8(double x8) {
            this.x8 = x8;
        }

        public double getX9() {
            return x9;
        }

        public void setX9(double x9) {
            this.x9 = x9;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Data)) return false;
            Data data = (Data) o;
            return number == data.number &&
                    Double.compare(data.x1, x1) == 0 &&
                    Double.compare(data.x2, x2) == 0 &&
                    Double.compare(data.x3, x3) == 0 &&
                    Double.compare(data.x4, x4) == 0 &&
                    Double.compare(data.x5, x5) == 0 &&
                    Double.compare(data.x6, x6) == 0 &&
                    Double.compare(data.x7, x7) == 0 &&
                    Double.compare(data.x8, x8) == 0 &&
                    Double.compare(data.x9, x9) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(number, x1, x2, x3, x4, x5, x6, x7, x8, x9);
        }
    }
}
