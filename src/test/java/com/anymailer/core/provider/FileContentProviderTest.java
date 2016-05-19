package com.anymailer.core.provider;

import com.anymailer.core.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by noel on 23/12/15.
 */
public class FileContentProviderTest
{

    @Test
    public void getContentProvider()
    {
        IContentProvider contentProvider = new FileContentProvider("build/resources/test/test.txt");
        ContentProviderFactory.setContentProvider(contentProvider);
        IContentProvider builtContentProvider = ContentProviderFactory.buildContentProvider();
        Assert.assertEquals(contentProvider, builtContentProvider);
    }

    @Test
    public void loadFile()
    {
        IContentProvider contentProvider = new FileContentProvider("build/resources/test/test.txt");
        List<String> content = contentProvider.retrieveContentAsText("");
        for (String line: content)
            Logger.console.info(line);
    }

}