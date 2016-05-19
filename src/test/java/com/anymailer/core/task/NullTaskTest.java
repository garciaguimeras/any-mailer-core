package com.anymailer.core.task;

import com.anymailer.core.Logger;
import org.junit.Test;

/**
 * Created by noel on 16/12/15.
 */
public class NullTaskTest
{

    @Test
    public void getHelpText()
    {
        NullTask task = new NullTask();
        task.processMessage("help");
        String result = task.getMailText();
        Logger.console.info(result);
    }

}
