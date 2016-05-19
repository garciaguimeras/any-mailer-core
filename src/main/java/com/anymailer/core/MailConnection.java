package com.anymailer.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.util.SharedByteArrayInputStream;

import com.sun.mail.imap.IMAPStore;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.jsoup.Jsoup;

public class MailConnection 
{
	
	private static MailConnection instance;
	
	private String username;
	private String password;
	
    private Session receiverSession = null;
    private Session senderSession = null;
	private Store store = null;
    
    private MailConnection(final String username, final String password)
    {
    	this.username = username;
    	this.password = password;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password",  password);
        senderSession = Session.getInstance(props, new Authenticator() 
        {
        	protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
		});
    }
    
    public static MailConnection newInstance(String username, String password)
    {
   		instance = new MailConnection(username, password);
    	return instance; 	
    }
    
    public static MailConnection getInstance()
    {
    	return instance; 	
    }
    
    public boolean connectIMAP()
    {
        try 
        {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            receiverSession = Session.getInstance(props, null);
            store = receiverSession.getStore();
            store.connect("imap.gmail.com", username, password);
		}
        catch (Exception e)
        {
            e.printStackTrace();
			return false;
		}
        return true;
    }
    
    public void sendMail(Message message)
    {
    	try
    	{
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

            Transport transport = senderSession.getTransport("smtp");
	        transport.connect("smtp.gmail.com", 587, username, password);
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public List<String> getFolders()
    {
    	ArrayList<String> list = new ArrayList<String>();
    	try
    	{
	        Folder[] folders = store.getDefaultFolder().list();
	        for (Folder f: folders)
	        	list.add(f.getFullName());
    	}
    	catch (Exception e)
    	{}
    	return list;
    }
    
    public Folder openFolder(String folderName)
    {
    	try
    	{
	        Folder folder = store.getDefaultFolder();
	        folder = folder.getFolder(folderName);
	        if (folder == null)
	            return null;
            folder.open(Folder.READ_WRITE);
	        return folder;
    	}
    	catch (Exception e)
    	{
            e.printStackTrace();
        }
    	return null;
    }
    
    public Message[] getUnreadMessages(Folder folder)
    {
    	FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
    	try
    	{
    		Message[] messages = folder.search(ft);
    		return messages;
    	}
    	catch (Exception e)
    	{}
    	return new Message[] {};
    }

    private String getUTF8Content(Object contentObject)
    {
        SharedByteArrayInputStream sbais = (SharedByteArrayInputStream) contentObject;
        InputStreamReader isr = new InputStreamReader(sbais, Charset.forName("UTF-8"));
        int charsRead = 0;
        StringBuilder content = new StringBuilder();
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];

        try
        {
            while ((charsRead = isr.read(buffer)) != -1)
            {
                content.append(Arrays.copyOf(buffer, charsRead));
            }
        }
        catch (Exception e)
        {}

        return content.toString();
    }

    private String getMessageContentAsMultipart(Multipart content)
    {
        String result = null;

        try
        {
            int count = content.getCount();
            for (int i = 0; i < count; i++)
            {
                BodyPart part = content.getBodyPart(i);
                if (part.isMimeType("text/plain"))
                {
                    result = (String) part.getContent();
                    break;
                }
                else if (part.isMimeType("text/html"))
                {
                    String html = (String) part.getContent();
                    result = Jsoup.parse(html).text();
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    private String getMessageContentAsInputStream(InputStream is)
    {
        String result = null;
        byte[] array = null;

        try
        {
            array = IOUtils.toByteArray(is);
        }
        catch (Exception e)
        {}

        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(array);
            MimeMultipart multipart = new MimeMultipart(new ByteArrayDataSource(bais, "multipart/mixed"));

            result = null;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.getContentType().startsWith("text/plain")) {
                    result = getUTF8Content(part.getContent());
                    break;
                }
                if (part.getContentType().startsWith("text/html")) {
                    String html = getUTF8Content(part.getContent());
                    result = Jsoup.parse(html).text();
                    break;
                }
            }
        }
        catch (Exception e)
        {
            result = null;
        }

        try
        {
            if (result == null)
                result = IOUtils.toString(array, "utf-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public String getFirstLine(String text)
    {
        String[] lines = text.split("[\\r\\n]+");
        for (String line: lines)
        {
            line = line.trim();
            if (line.length() > 0)
                return line;
        }
        return "";
    }

	public String getMessageContent(Message message)
    {
        String result = null;
        try
        {
            if (message instanceof MimeMessage)
            {
                MimeMessage m = (MimeMessage) message;
                Object contentObject = m.getContent();
                if (contentObject instanceof Multipart)
                {
                    result = getMessageContentAsMultipart((Multipart) contentObject);
                }
                else if (contentObject instanceof String) // a simple text message
                {
                    result = (String) contentObject;
                }
                else if (contentObject instanceof InputStream) // an input stream
                {
                    result = getMessageContentAsInputStream((InputStream) contentObject);
                }
            }
            else // not a mime message
            {
                result = null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result != null ? getFirstLine(result) : "";
    }

    public Message createReplyMessage(MailMessage originalMessage, String subject, String body)
    {
        try
        {
            Message message = new MimeMessage(senderSession);
            message.setFrom(originalMessage.getTo());
            message.setRecipients(Message.RecipientType.TO, new Address[]{originalMessage.getFrom()});
            message.setSubject(subject);

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);

            message.setContent(multipart);

            return message;
        }
        catch (Exception e)
        {}

        return null;
    }

    public void addAttachmentToMessage(Message message, String filename, String mimeType, Object attachment)
    {
        try
        {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(attachment, mimeType));
            attachmentPart.setFileName(filename);

            Multipart multipart = (Multipart) message.getContent();
            multipart.addBodyPart(attachmentPart);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void disconnect()
    {
        try
        {
            store.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}