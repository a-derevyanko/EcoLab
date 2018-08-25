package org.ecolab.server.service.impl.content;

import org.ecolab.server.common.Profiles;
import org.ecolab.server.service.api.content.LabMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.util.Locale;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
@Profile(Profiles.ADDITIONS.EMAIL)
public class LabMailServiceImpl implements LabMailService {

    protected final JavaMailSender mailSender;

    protected final MessageSource messageSource;

    @Autowired
    public LabMailServiceImpl(JavaMailSender mailSender, MessageSource messageSource) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
    }

    @Override
    public void sentInitialDataToEmail(byte[] variant, Locale locale, String email) throws MailException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom("ecolabserver@gmail.com");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(messageSource.getMessage("report.initial-data.email-data-in-attach", null, locale));
            mimeMessageHelper.setSubject(messageSource.getMessage("report.initial-data.email-subject", null, locale));
            mimeMessageHelper.addAttachment("initialData.pdf", new ByteArrayDataSource(variant, MediaType.APPLICATION_PDF_VALUE));

        } catch (MessagingException e) {
            throw new MailPreparationException(e.getLocalizedMessage(), e);
        }
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendReportToEmail(byte[] labData, Locale locale, String email) throws MailException {

    }
}
