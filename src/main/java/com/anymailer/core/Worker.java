package com.anymailer.core;

import com.anymailer.core.task.ITask;
import com.anymailer.core.task.TaskFactory;

import javax.mail.Address;
import javax.mail.Message;

/**
 * Created by noel on 8/12/15.
 */
public class Worker implements Runnable
{

    MailMessage message;

    public Worker(MailMessage message)
    {
        this.message = message;
    }

    public void startThread()
    {
        new Thread(this).start();
    }

    private String getReadableText(String text)
    {
        text = text.replace("\r\n", " ");
        text = text.replace("\r", " ");
        text = text.replace("\n", " ");
        if (text.length() > 255)
        {
            text = text.substring(0, 255) + "...";
        }
        return text;
    }

    @Override
    public void run()
    {
        try
        {
            MailConnection mailConnection = MailConnection.getInstance();
            String content = message.getContent();
            String subject = message.getSubject();
            String from = message.getFrom().toString();

            Logger.console.info("Incoming mail: " + from + subject + "// " + content);
            Logger.traffic.info("IN" + "\t" + from + "\t" + subject + "\t" + getReadableText(content));

            ITask task = TaskFactory.buildTask(subject);
            task.processMessage(content);
            Message replyMessage = mailConnection.createReplyMessage(message, task.getMailSubject(), task.getMailText());
            ITask.MailAttachment mailAttachment = task.getMailAttachment();
            if (mailAttachment != null)
                mailConnection.addAttachmentToMessage(replyMessage, mailAttachment.filename, mailAttachment.mimeType, mailAttachment.attachment);
            mailConnection.sendMail(replyMessage);

            Logger.console.info("Reply sent with text: " + task.getMailText());
            Logger.traffic.info("OUT" + "\t" + from + "\t" + task.getMailSubject() + "\t" + getReadableText(task.getMailText()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
