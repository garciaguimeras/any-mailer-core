package com.anymailer.core.task;

import com.anymailer.core.MailTextBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by noel on 19/12/15.
 */
public class StackOverflowTask extends HttpTask
{

    private static final String BASE_URL = "http://www.stackoverflow.com";
    private static final String SEARCH_URL = BASE_URL + "/search?q=";

    String responseText = "";

    @Override
    public void processMessage(String searchText)
    {
        if (searchText.equals(""))
        {
            responseText = getDefaultErrorText();
            return;
        }

        String url = SEARCH_URL + splitAndJoin(searchText, "+");
        List<String> content = retrieveUrlText(url);

        Pattern pattern1 = Pattern.compile("<a href=\"([\\s\\S\\w\\W\\d\\D]+)\" data-searchsession=\"([\\s\\S\\w\\W\\d\\D]+)\" title=\"([\\s\\S\\w\\W\\d\\D]+)\">");
        Pattern pattern2 = Pattern.compile("<h3><a href=\"([\\s\\S\\w\\W\\d\\D]+)\" class=\"question-hyperlink\">([\\s\\S\\w\\W\\d\\D]+)</a></h3>");
        MailTextBuilder mailTextBuilder = new MailTextBuilder();
        for (String line: content)
        {
            Matcher matcher = pattern1.matcher(line);
            if (matcher.find())
            {
                mailTextBuilder.addLine(matcher.group(3));
                mailTextBuilder.addLine("URL: {0}{1}", BASE_URL, matcher.group(1));
                mailTextBuilder.addLine();
            }

            matcher = pattern2.matcher(line);
            if (matcher.find())
            {
                mailTextBuilder.addLine(matcher.group(2));
                mailTextBuilder.addLine("URL: {0}{1}", BASE_URL, matcher.group(1));
                mailTextBuilder.addLine();
            }
        }

        responseText = mailTextBuilder.toString();
    }

    @Override
    public String getMailSubject()
    {
        return "StackOverflow";
    }

    @Override
    public String getMailText()
    {
        return responseText;
    }

    @Override
    public MailAttachment getMailAttachment()
    {
        return null;
    }
}
