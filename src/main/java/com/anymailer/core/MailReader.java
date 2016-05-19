package com.anymailer.core;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;

/**
 * Created by noel on 8/12/15.
 */
public class MailReader implements Runnable
{

    private boolean running;

    public void startThread()
    {
        new Thread(this).start();
    }

    public void stopThread()
    {
        running = false;
    }

    public boolean isRunning()
    {
        return running;
    }

    @Override
    public void run()
    {
        MailConnection mailConnection = MailConnection.newInstance("emailaccount@xxxxx.com", "password");

        Logger.console.info("== Ready ==");
        running = true;
        while (running)
        {
            try
            {
                mailConnection.connectIMAP();
                Folder folder = mailConnection.openFolder("INBOX");
                Message[] unread = mailConnection.getUnreadMessages(folder);
                for (Message m : unread)
                {
                    m.setFlag(Flags.Flag.SEEN, true);
                    MailMessage mm = new MailMessage(m);
                    new Worker(mm).startThread();
                }
                mailConnection.disconnect();

                // Sleep for 10 secs
                Thread.sleep(10 * 1000);
            }
            catch (Exception e)
            {}
        }
        Logger.console.info("== Bye ==");
    }
}
