package com.anymailer.core.task;

import com.anymailer.core.Logger;
import com.anymailer.core.provider.ContentProviderFactory;
import com.anymailer.core.provider.FileContentProvider;
import org.junit.Test;

/**
 * Created by noel on 21/12/15.
 */
public class StackOverflowTaskTest
{

    @Test
    public void retrieveWebPage()
    {
        StackOverflowTask task = new StackOverflowTask();
        task.processMessage("java nullpointerexception");
        String result = task.getMailText();
        Logger.console.info(result);
    }

    @Test
    public void retrieveFromFile()
    {
        ContentProviderFactory.setContentProvider(new FileContentProvider("build/resources/test/stackoverflow.html"));

        StackOverflowTask task = new StackOverflowTask();
        task.processMessage("java nullpointerexception");
        String result = task.getMailText();
        Logger.console.info(result);
    }

}
