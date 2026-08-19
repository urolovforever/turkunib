package com.tiu.turk.authentication.web.service;

import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.mail.internet.MimeMessage;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * Sends the auth-flow e-mails (password reset, account activation). If no SMTP
 * server is configured (spring.mail.host absent → no JavaMailSender bean), the
 * link is written to the application log instead so the flow stays testable.
 */
@Service
public class MailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final MessageSource messages;

    @Value("${app.mail.from:TURKUNIB <noreply@turkunib.org>}")
    private String from;

    public MailService(ObjectProvider<JavaMailSender> mailSenderProvider, MessageSource messages) {
        this.mailSenderProvider = mailSenderProvider;
        this.messages = messages;
    }

    public void sendPasswordReset(UserEntity user, String link, Locale locale) {
        String subject = msg("mail.reset.subject", locale);
        String html = buildEmail(user, locale,
                msg("mail.reset.intro", locale),
                msg("mail.reset.button", locale),
                link,
                msg("mail.reset.expiry", locale),
                msg("mail.reset.ignore", locale));
        send(user.getEmail(), subject, html, link);
    }

    public void sendActivation(UserEntity user, String link, Locale locale) {
        String subject = msg("mail.activation.subject", locale);
        String html = buildEmail(user, locale,
                msg("mail.activation.intro", locale),
                msg("mail.activation.button", locale),
                link,
                msg("mail.activation.expiry", locale),
                null);
        send(user.getEmail(), subject, html, link);
    }

    private void send(String to, String subject, String html, String link) {
        JavaMailSender sender = this.mailSenderProvider.getIfAvailable();
        if (sender == null) {
            log.warn("MAIL DISABLED (no spring.mail.host configured) — would send to {} [{}]. Link: {}", to, subject, link);
            return;
        }
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(this.from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            sender.send(message);
            log.info("Auth mail sent to {} [{}]", to, subject);
        } catch (Exception e) {
            // The auth flow must not break on SMTP hiccups; the user can use the
            // resend / forgot-password pages to get a new link.
            log.error("Failed to send auth mail to {} [{}]: {}", to, subject, e.getMessage());
        }
    }

    private String buildEmail(UserEntity user, Locale locale, String intro, String buttonLabel,
                              String link, String expiry, String ignoreNote) {
        String hello = this.messages.getMessage("mail.hello",
                new Object[]{HtmlUtils.htmlEscape(user.getFirstName() == null ? "" : user.getFirstName())}, locale);
        String fallback = msg("mail.fallback", locale);
        String footer = msg("mail.footer", locale);
        String safeLink = HtmlUtils.htmlEscape(link);

        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"background:#f2f5f9;padding:32px 12px;font-family:Arial,Helvetica,sans-serif;\">")
          .append("<div style=\"max-width:560px;margin:0 auto;background:#ffffff;border-radius:12px;overflow:hidden;")
          .append("border:1px solid #e3e8ef;\">")
          .append("<div style=\"background:#36348E;padding:18px 28px;\">")
          .append("<span style=\"color:#ffffff;font-size:20px;font-weight:bold;letter-spacing:1px;\">TURKUNIB</span>")
          .append("</div>")
          .append("<div style=\"height:4px;background:#FFC53A;\"></div>")
          .append("<div style=\"padding:28px;color:#333;font-size:15px;line-height:1.6;\">")
          .append("<p style=\"margin:0 0 14px;\">").append(hello).append("</p>")
          .append("<p style=\"margin:0 0 22px;\">").append(intro).append("</p>")
          .append("<p style=\"text-align:center;margin:0 0 22px;\">")
          .append("<a href=\"").append(safeLink).append("\" style=\"display:inline-block;background:#36348E;color:#ffffff;")
          .append("padding:12px 32px;border-radius:8px;text-decoration:none;font-weight:bold;\">")
          .append(buttonLabel).append("</a></p>")
          .append("<p style=\"margin:0 0 10px;color:#777;font-size:13px;\">").append(expiry).append("</p>");
        if (ignoreNote != null) {
            sb.append("<p style=\"margin:0 0 10px;color:#777;font-size:13px;\">").append(ignoreNote).append("</p>");
        }
        sb.append("<p style=\"margin:16px 0 4px;color:#777;font-size:13px;\">").append(fallback).append("</p>")
          .append("<p style=\"margin:0;word-break:break-all;font-size:13px;\"><a href=\"").append(safeLink)
          .append("\" style=\"color:#36348E;\">").append(safeLink).append("</a></p>")
          .append("</div>")
          .append("<div style=\"padding:14px 28px;background:#f8f9fb;border-top:1px solid #eef1f5;")
          .append("color:#999;font-size:12px;text-align:center;\">").append(footer).append("</div>")
          .append("</div></div>");
        return sb.toString();
    }

    private String msg(String key, Locale locale) {
        return this.messages.getMessage(key, null, locale);
    }
}
