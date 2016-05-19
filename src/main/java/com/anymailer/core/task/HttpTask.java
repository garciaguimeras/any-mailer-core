package com.anymailer.core.task;

import com.anymailer.core.MailTextBuilder;
import com.anymailer.core.provider.ContentProviderFactory;
import com.anymailer.core.provider.IContentProvider;

import java.util.List;

/**
 * Created by noel on 12/12/15.
 */
public abstract class HttpTask implements ITask
{

    protected List<String> retrieveUrlText(String urlStr)
    {
        IContentProvider contentProvider = ContentProviderFactory.buildContentProvider();
        return contentProvider.retrieveContentAsText(urlStr);
    }

    protected byte[] retrieveUrlContent(String urlStr)
    {
        IContentProvider contentProvider = ContentProviderFactory.buildContentProvider();
        return contentProvider.retrieveContent(urlStr);
    }

    protected String splitAndJoin(String text, String joinStr)
    {
        String[] words = text.split(" ");
        String result = "";
        for (String w: words)
        {
            w = w.trim();
            if (!w.equals(""))
            {
                if (result.equals(""))
                    result = w;
                else
                    result += joinStr + w;
            }
        }
        return result;
    }

    protected String getDefaultErrorText()
    {
        MailTextBuilder builder = new MailTextBuilder();
        builder.addLine("No se especifico ninguna busqueda.");
        builder.addLine("Por favor escriba en el cuerpo del correo el texto que desea buscar.");
        return builder.toString();
    }

}
