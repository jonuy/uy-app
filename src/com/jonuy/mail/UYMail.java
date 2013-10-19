package com.jonuy.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

/**
 * Courtesy of the blog post that was used for reference:
 *  http://www.jondev.net/articles/Sending_Emails_without_User_Intervention_(no_Intents)_in_Android
 * And the StackOverflow answer here:
 *  http://stackoverflow.com/a/2033124
 */
public class UYMail extends Authenticator {
    private String mHost;
    private String mPort;
    private String mSocketPort;
    private boolean mAuth;

    // Sender credentials
    private String mSenderEmail;
    private String mSenderPass;

    /**
     * Create UYMail object for sending emails
     *
     * @param email Email of the sender
     * @param password Password for the sender's email
     */
    public UYMail(String email, String password) {
        mHost = "smtp.gmail.com";
        mPort = "465";
        mSocketPort = "465";

        mSenderEmail = email;
        mSenderPass = password;

        mAuth = true;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mSenderEmail, mSenderPass);
    }

    /**
     * TODO
     *
     * @param to
     * @param from
     * @param subject
     * @param body
     * @return
     * @throws Exception
     */
    public boolean send(String to, String from, String subject, String body) throws Exception {
        if (mSenderEmail == null || mSenderEmail.length() == 0
                || mSenderPass == null || mSenderPass.length() == 0
                || to == null || to.length() == 0
                || from == null || from.length() == 0
                || body == null || body.length() == 0) {
            return false;
        }

        // TODO: comments about Properties
        Properties properties = new Properties();

        properties.put("mail.smtp.host", mHost);
        if (mAuth) {
            properties.put("mail.smtp.auth", "true");
        }

        properties.put("mail.smtp.port", mPort);
        properties.put("mail.smtp.socketFactory.port", mSocketPort);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        // TODO: comment about Session
        Session session = Session.getInstance(properties, this);

        MimeMessage msg = new MimeMessage(session);

        // TODO: comment
        msg.setFrom(new InternetAddress(from));

        // TODO: comment
        InternetAddress addressTo = new InternetAddress(to);
        msg.setRecipient(MimeMessage.RecipientType.TO, addressTo);

        msg.setSubject(subject);
        msg.setSentDate(new Date());

        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        msg.setDataHandler(handler);

        // Send the email
        Transport.send(msg);

        return true;
    }
}
