package com.anymailer.core.task;

/**
 * Created by noel on 12/12/15.
 */
public interface ITask
{

    class MailAttachment
    {
        public String filename;
        public String mimeType;
        public Object attachment;

        public MailAttachment(String filename, String mimeType, Object attachment)
        {
            this.filename = filename;
            this.mimeType = mimeType;
            this.attachment = attachment;
        }
    }

    void processMessage(String searchText);
    String getMailSubject();
    String getMailText();
    MailAttachment getMailAttachment();

}
