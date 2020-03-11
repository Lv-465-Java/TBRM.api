package com.softserve.rms.util;

import com.softserve.rms.dto.group.MemberDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.handler.CustomAsyncExceptionHandler;
import com.softserve.rms.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSender {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;

    @Autowired
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends email asynchronously
     *
     * @param subject subject of email
     * @param text message
     * @param recipient users
     * @author Marian Dutchyn
     */
    @Async
    public void sendEmail(String subject, String text, String... recipient) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mailMessage, true);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText("<html><body><h3>" + text +"</h3> </body></html>", true);
            mailSender.send(mailMessage);
        } catch (MessagingException | MailException e) {
            LOG.error("Can not send email: " + e.getMessage());
        }
    }
}
