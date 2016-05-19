package com.anymailer.core.provider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by noel on 23/12/15.
 */
public class FileContentProvider extends InputStreamContentProvider
{

    String filename;

    public FileContentProvider(String filename)
    {
        this.filename = filename;
    }

    @Override
    protected InputStream retrieve(String urlStr) throws Exception
    {
        return new FileInputStream(filename);
    }

}
