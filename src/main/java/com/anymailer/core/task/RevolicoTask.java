package com.anymailer.core.task;

import com.anymailer.core.MailTextBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by noel on 12/12/15.
 */
public class RevolicoTask extends HttpTask
{
    private static final String BASE_URL = "http://clasificados.zapto.org/search.html?q=";

    private String responseText = "";

    @Override
    public void processMessage(String searchText)
    {
        if (searchText.equals(""))
        {
            responseText = getDefaultErrorText();
            return;
        }

        String url = BASE_URL + splitAndJoin(searchText, "+");
        List<String> content = retrieveUrlText(url);

        Pattern pattern1 = Pattern.compile("<span>([\\s\\S\\w\\W\\d\\D]+)</span>");
        Pattern pattern2 = Pattern.compile("([\\s\\S\\w\\W\\d\\D]+)</a>");
        Pattern pattern3 = Pattern.compile("<span style=\"margin-left: 5px;\" class=\"textGray\">([\\s\\S\\w\\W\\d\\D]+)");

        MailTextBuilder mailTextBuilder = new MailTextBuilder();
        int matchPhase = 0;
        String text = "";
        for (String line: content)
        {
            if (matchPhase == 2)
            {
                Matcher matcher = pattern3.matcher(line);
                if (matcher.find())
                {
                    matchPhase = 0;
                    text += " // " + matcher.group(1).trim();

                    if (text.endsWith("</span>"))
                        text = text.substring(0, text.length() - 7);
                    if (!text.endsWith("..."))
                        text += "...";
                    text.replaceAll("\\s+", " ");

                    mailTextBuilder.addLine(text);
                    mailTextBuilder.addLine();
                    text = "";
                }
            }

            if (matchPhase == 1)
            {
                Matcher matcher = pattern2.matcher(line);
                if (matcher.find())
                {
                    matchPhase = 2;
                    text += " " + matcher.group(1).trim();
                }
            }

            if (matchPhase == 0)
            {
                Matcher matcher = pattern1.matcher(line);
                if (matcher.find())
                {
                    matchPhase = 1;
                    text = matcher.group(1).trim();
                }
            }
        }

        responseText = mailTextBuilder.toString();
    }

    @Override
    public String getMailSubject()
    {
        return "Revolico";
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
