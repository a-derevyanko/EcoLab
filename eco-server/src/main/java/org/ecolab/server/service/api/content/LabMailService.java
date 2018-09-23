package org.ecolab.server.service.api.content;

import org.springframework.mail.MailException;

import java.util.Locale;

public interface LabMailService {
    /**
     * Отправляет печатный вариант исходных данных в PDF формате на почту
     * @param variant вариант лабораторной работы
     * @param email адрес
     */
    void sentInitialDataToEmail(byte[] variant, String email) throws MailException;


    /**
     * Отправляет печатный вариант отчёта в PDF формате на почту
     * @param labData данные лабораторной работы
     * @param email адрес
     */
    void sendReportToEmail(byte[] labData, String email) throws MailException;
}
