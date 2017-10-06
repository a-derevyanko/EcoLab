package org.ekolab.server.service.api.content;

import org.springframework.mail.MailException;

import java.util.Locale;

public interface LabMailService {
    /**
     * Отправляет печатный вариант исходных данных в PDF формате на почту
     * @param variant вариант лабораторной работы
     * @param locale язык
     * @param email адрес
     */
    void sentInitialDataToEmail(byte[] variant, Locale locale, String email) throws MailException;


    /**
     * Отправляет печатный вариант отчёта в PDF формате на почту
     * @param labData данные лабораторной работы
     * @param locale язык
     * @param email адрес
     */
    void sendReportToEmail(byte[] labData, Locale locale, String email) throws MailException;
}
