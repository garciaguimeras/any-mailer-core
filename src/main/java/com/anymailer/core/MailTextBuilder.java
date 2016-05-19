package com.anymailer.core;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by noel on 16/12/15.
 */
public class MailTextBuilder
{

    ArrayList<String> fullText = new ArrayList<String>();

    public String formatText(String text, String... params)
    {
        Pattern pattern = Pattern.compile("\\{(\\d+)\\}");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find())
        {
            int pos = Integer.valueOf(matcher.group(1));
            if (pos >= 0 && pos < params.length)
                text = text.replace("{" + pos + "}", params[pos]);
        }

        return text;
    }

    public void addLine(String text, String... params)
    {
        fullText.add(formatText(text, params));
    }

    public void addLine()
    {
        fullText.add("");
    }

    private String addCopyrightInfo(String response)
    {
        response += "\r\n";
        response += "\r\n";
        response += "(c) Copyright 2016\r\n";
        response += "\r\n";
        response += "Los contenidos que se muestran en este mensaje de correo son tomados de forma total o parcial de sitios web de terceros, y expresan las opiniones de los autores y/o usuarios de estos sitios.\r\n";
        response += "Los creadores de esta aplicacion no se responsabilizan, apoyan o se oponen a las opiniones o criterios expresados en el contenido de este mensaje.\r\n";
        return response;
    }

    @Override
    public String toString()
    {
        String response = "";

        for (String text: fullText)
        {
            response += text + "\r\n";
        }
        response = addCopyrightInfo(response);

        return response;
    }

}
