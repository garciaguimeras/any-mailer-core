package com.anymailer.core.task;

import com.anymailer.core.Logger;
import com.anymailer.core.provider.ContentProviderFactory;
import com.anymailer.core.provider.FileContentProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by noel on 15/12/15.
 */

public class RevolicoTaskTest
{

    @Test
    public void retrieveWebPage()
    {
        RevolicoTask task = new RevolicoTask();
        task.processMessage("casa");
        String result = task.getMailText();
        Logger.console.info(result);
    }

    @Test
    public void retrieveFromFile()
    {
        ContentProviderFactory.setContentProvider(new FileContentProvider("build/resources/test/revolico.html"));

        RevolicoTask task = new RevolicoTask();
        task.processMessage("casa");
        String result = task.getMailText();
        Logger.console.info(result);
    }

}
