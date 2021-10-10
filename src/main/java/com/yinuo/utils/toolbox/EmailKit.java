package com.yinuo.utils.toolbox;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 简单的邮件封装
 *
 * @author liang
 */
public class EmailKit {
    public static final String MAIL_SMTP_SOCKS_HOST = "mail.smtp.socks.host";
    public static final String MAIL_SMTP_SOCKS_PORT = "mail.smtp.socks.port";
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_DEBUG = "mail.debug";
    public static final String PROTOCOL_SMTP = "smtp";

    /**
     * 设置false后则所有邮件不会真正发送
     */
    private boolean open = true;
    private boolean htmlSupport = false;
    private boolean debug = false;

    /**
     * Email Account
     */
    private String server;
    private String from;
    private String user;
    private String pass;
    private String port;

    /**
     * Proxy
     */
    private boolean proxy = false;
    private String proxyHost = "";
    private String proxyPort = "";

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public EmailKit(String server, String port) {
        super();
        this.server = server;
        this.port = port;
    }

    public EmailKit(String server, String user, String pass, String port) {
        super();
        this.server = server;
        this.from = user;
        this.user = user;
        this.pass = pass;
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void setProxy(String proxyHost, String proxyPort) {
        this.proxy = true;
        this.proxyPort = proxyPort;
        this.proxyHost = proxyHost;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public EmailKit(String host, String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.from = user;
    }

    public boolean isHtmlSupport() {
        return htmlSupport;
    }

    public void setHtmlSupport(boolean htmlSupport) {
        this.htmlSupport = htmlSupport;
    }

    public void send(String subject, String content,
                     String[] toArray,
                     String[] ccArray,
                     String[] attachmentArray) throws Exception {
        send(subject, content, toArray, ccArray, null, attachmentArray);
    }

    public void send(String subject, String content,
                     String[] toArray,
                     String[] ccArray,
                     String[] bcArray,
                     String[] attachmentArray) throws Exception {
        List<String> to = toArray == null ? null : Arrays.asList(toArray);
        List<String> cc = ccArray == null ? null : Arrays.asList(ccArray);
        List<String> bc = bcArray == null ? null : Arrays.asList(bcArray);
        List<String> at = attachmentArray == null ? null : Arrays.asList(attachmentArray);
        send(subject, content, to, cc, bc, at);
    }

    public void send(String subject, String content,
                     List<String> toList,
                     List<String> ccList,
                     String attachment) throws Exception {
        List<String> attachments = new ArrayList<String>();
        attachments.add(attachment);
        send(subject, content, toList, ccList, null, attachments);
    }

    public void send(String subject, String content,
                     List<String> toList,
                     List<String> ccList,
                     List<String> bcList,
                     List<String> attachments) throws Exception {
        if (!open) {
            return;
        }

        // build session with props
        Session session = Session.getDefaultInstance(buildProperties());
        // output debug info to console
        session.setDebug(false);

        // build message object with session
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        setRecipients(toList, ccList, bcList, message);

        message.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));

        Multipart multipart = new MimeMultipart();

        BodyPart contentPart = new MimeBodyPart();
        if (htmlSupport) {
            contentPart.setContent(content, "text/html; charset=utf-8");
        } else {
            contentPart.setText(content);
        }
        multipart.addBodyPart(contentPart);

        setAttachment(multipart, attachments);

        // put multipart to message
        message.setContent(multipart);
        // save email
        message.saveChanges();

        // send email
        Transport transport = null;
        try {
            transport = session.getTransport(PROTOCOL_SMTP);
            transport.connect(server, user, pass);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            throw e;
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }

    private void setAttachment(Multipart multipart, List<String> attachments) throws MessagingException, UnsupportedEncodingException {
        if (attachments != null) {
            // add attachment
            for (String fullPath : attachments) {
                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fullPath);
                // add attachment
                messageBodyPart.setDataHandler(new DataHandler(source));
                // add attachment title
                // important! using Base64 code transfer to avoid messy code
                // sun.misc.BASE64Encoder enc = new
                // sun.misc.BASE64Encoder();
                // messageBodyPart.setFileName("=?UTF-8?B?" +
                // enc.encode(file.getBytes()) + "?=");
                messageBodyPart.setFileName(MimeUtility.encodeText(new File(fullPath).getName()));
                multipart.addBodyPart(messageBodyPart);
            }
        }
    }

    private void setRecipients(List<String> toList, List<String> ccList, List<String> bcList, MimeMessage message) throws MessagingException {
        if (toList != null) {
            for (String to : toList) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }
        }
        if (ccList != null) {
            for (String to : ccList) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(to));
            }
        }
        if (bcList != null) {
            for (String to : bcList) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(to));
            }
        }
    }

    private Properties buildProperties() {
        Properties props = new Properties();
        if (isProxy()) {
            props.put(MAIL_SMTP_SOCKS_HOST, this.proxyHost);
            props.put(MAIL_SMTP_SOCKS_PORT, this.proxyPort);
        }
        // set
        props.put(MAIL_SMTP_HOST, server);
        props.put(MAIL_SMTP_PORT, port);
        // set authentication needed. means we should set username and
        // password.!important!
        props.put(MAIL_SMTP_AUTH, "true");
        props.put(MAIL_DEBUG, "true");
        return props;
    }
}
