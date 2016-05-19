package com.anymailer.core.provider;

import java.util.List;

/**
 * Created by noel on 19/12/15.
 */
public interface IContentProvider
{

    List<String> retrieveContentAsText(String urlStr);
    byte[] retrieveContent(String urlStr);

}
