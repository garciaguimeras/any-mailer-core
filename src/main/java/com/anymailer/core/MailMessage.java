package com.anymailer.core;

import javax.mail.Address;
import javax.mail.Message;

/**
 * Created by noel on 16/12/15.
 */
public class MailMessage
{

    Address from;
    Address to;
    String subject;
    String content;

    public MailMessage(Message message)
    {
        try
        {
            this.from = message.getFrom()[0];
            this.to = message.getAllRecipients()[0];
            this.subject = message.getSubject();
            this.content = MailConnection.getInstance().getMessageContent(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public MailMessage(Address from, Address to, String subject, String content)
    {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public Address getFrom()
    {
        return from;
    }

    public Address getTo()
    {
        return to;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getContent()
    {
        return content;
    }
}
