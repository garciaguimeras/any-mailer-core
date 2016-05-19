package com.anymailer.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by noel on 20/12/15.
 */
public class MailTextBuilderTest
{

    @Test
    public void textFormat()
    {
        MailTextBuilder builder = new MailTextBuilder();

        String txt = "Hello {0}. This is a {1}";
        String result = builder.formatText(txt, "World", "test");

        Assert.assertEquals("Hello World. This is a test", result);
    }

}
