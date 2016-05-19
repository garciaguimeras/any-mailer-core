package com.anymailer.core.task;

import com.anymailer.core.MailTextBuilder;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by noel on 20/12/15.
 */
public class DownloadTask extends HttpTask
{

    String responseText = "";
    MailAttachment attachment = null;

    public String getPageNotFoundErrorText(String url)
    {
        MailTextBuilder builder = new MailTextBuilder();
        builder.addLine("La URL solicitada no se encuentra disponible.");
        builder.addLine("Probablemente la URL no existe, o el contenido solicitado es muy grande para enviar por correo.");
        builder.addLine("URL: {0}", url);
        return builder.toString();
    }

    public String getDownloadErrorText(String url)
    {
        MailTextBuilder builder = new MailTextBuilder();
        builder.addLine("No se pudo descargar la URL solicitada.");
        builder.addLine("URL: {0}", url);
        return builder.toString();
    }

    @Override
    public void processMessage(String searchText)
    {
        if (searchText.equals(""))
        {
            responseText = getDefaultErrorText();
            return;
        }

        byte[] content = retrieveUrlContent(searchText);
        if (content.length == 0)
        {
            responseText = getPageNotFoundErrorText(searchText);
            return;
        }

        try
        {
            int pos = searchText.lastIndexOf("/");
            String filename = searchText.substring(pos + 1);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(baos);

            zip.putNextEntry(new ZipEntry(filename));
            zip.write(content);
            zip.closeEntry();

            zip.close();

            attachment = new MailAttachment(filename + ".zip", "application/zip", baos.toByteArray());
            responseText = new MailTextBuilder().toString();
        }
        catch (Exception e)
        {
            responseText = getDownloadErrorText(searchText);
        }
    }

    @Override
    public String getMailSubject()
    {
        return "Download";
    }

    @Override
    public String getMailText()
    {
        return responseText;
    }

    @Override
    public MailAttachment getMailAttachment()
    {
        return attachment;
    }
}
