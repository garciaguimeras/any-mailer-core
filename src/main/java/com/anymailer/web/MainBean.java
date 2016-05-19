package com.anymailer.web;

import com.anymailer.core.MailConnection;
import com.anymailer.core.MailReader;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 * Created by noel on 12/12/15.
 */
@ManagedBean(name = "main")
@SessionScoped
public class MainBean
{

    static MailReader reader = new MailReader();

    public boolean isRunning()
    {
        return reader.isRunning();
    }

    public void startService()
    {
        if (!reader.isRunning())
        {
            reader.startThread();
        }
    }

    public void stopService()
    {
        if (reader.isRunning())
        {
            reader.stopThread();
        }
    }

}
