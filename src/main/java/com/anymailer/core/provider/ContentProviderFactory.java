package com.anymailer.core.provider;

/**
 * Created by noel on 19/12/15.
 */
public class ContentProviderFactory
{

    private static IContentProvider contentProvider = null;

    public static void setContentProvider(IContentProvider provider)
    {
        contentProvider = provider;
    }

    public static IContentProvider buildContentProvider()
    {
        if (contentProvider != null)
            return contentProvider;

        // by default, get HttpContentProvider
        return new HttpContentProvider();
    }

}
