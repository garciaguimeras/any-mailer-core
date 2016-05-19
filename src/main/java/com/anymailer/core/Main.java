package com.anymailer.core;


import javax.mail.event.*;
import java.util.List;

public class Main
{

    public static void main(String[] args)
    {
        
        try
        {
            MailReader reader = new MailReader();
            reader.startThread();
            System.in.read();
            reader.stopThread();
        }
        catch(Exception e)
        {}
        
    }
    
}
