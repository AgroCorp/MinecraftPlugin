package me.sativus.testplugin.utils;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {
    private static Session session;
    private static String fromAddress;

    private EmailUtil() {
    }

    public static void initializeEmailUtil(String host, int port, String username, String password, String from) {
        Properties props = new Properties();

        if (username != null && password != null) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }

        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Authenticator authenticator = (username != null && password != null) ? new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        } : null;

        session = Session.getInstance(props, authenticator);
        fromAddress = from;
    }

    public static void sendEmail(String to, String subject, String body) {
        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From, To, Subject, and Body
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
