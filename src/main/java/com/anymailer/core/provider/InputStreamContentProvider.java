package com.anymailer.core.provider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noel on 23/12/15.
 */
public abstract class InputStreamContentProvider implements IContentProvider
{

    abstract InputStream retrieve(String resource) throws Exception;

    @Override
    public List<String> retrieveContentAsText(String urlStr)
    {
        ArrayList<String> result = new ArrayList<String>();
        try
        {
            InputStream is = retrieve(urlStr);
            LineIterator lineIterator = IOUtils.lineIterator(is, StandardCharsets.UTF_8);
            while (lineIterator.hasNext())
            {
                String line = lineIterator.nextLine().trim();
                line = StringEscapeUtils.unescapeHtml4(line);
                result.add(line);
            }
        }
        catch (Exception e)
        {
            result = new ArrayList<String>();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public byte[] retrieveContent(String urlStr)
    {
        byte[] result = new byte[] {};
        try
        {
            InputStream is = retrieve(urlStr);
            result = IOUtils.toByteArray(is);
        }
        catch (Exception e)
        {
            result = new byte[] {};
            e.printStackTrace();
        }
        return result;
    }

}
