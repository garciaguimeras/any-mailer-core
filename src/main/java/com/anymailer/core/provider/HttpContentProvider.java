package com.anymailer.core.provider;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by noel on 19/12/15.
 */
public class HttpContentProvider extends InputStreamContentProvider
{

    private static final long CONTENT_LENGTH_LIMIT = 5242880; // 5 mb

    @Override
    protected InputStream retrieve(String urlStr) throws Exception
    {
        boolean redirect = true;
        InputStream result = null;

        while (redirect)
        {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            connection.connect();

            redirect = false;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
            {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
                {
                    redirect = true;
                    urlStr = connection.getHeaderField("Location");
                }
            }

            if (!redirect)
            {
                long contentLength = connection.getContentLengthLong();
                if (contentLength <= CONTENT_LENGTH_LIMIT)
                    result = connection.getInputStream();
            }
        }

        return result;
    }

}
