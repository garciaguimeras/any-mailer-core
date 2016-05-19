package com.anymailer.core.task;

import com.anymailer.core.MailTextBuilder;

/**
 * Created by noel on 12/12/15.
 */
public class NullTask implements ITask
{
    private String getHelpText()
    {
        String mailAddr = "anymailsolutions@gmail.com";

        MailTextBuilder mailTextBuilder = new MailTextBuilder();

        mailTextBuilder.addLine("== INSTRUCCIONES PARA EL USO DEL SERVICIO ==");
        mailTextBuilder.addLine("Escriba un correo electronico a {0}", mailAddr);
        mailTextBuilder.addLine("Escriba en el asunto del correo el nombre del servicio deseado");
        mailTextBuilder.addLine("Escriba en el texto del correo la busqueda que desee realizar");
        mailTextBuilder.addLine("En el caso del servicio de descarga, escriba la URL en el texto del correo");
        mailTextBuilder.addLine("Descargue archivos pequenos. Los archivos se recibiran comprimidos con ZIP");
        mailTextBuilder.addLine("Las URL o textos de busqueda deben ser escritos en una unica linea (sin cambios de linea)");
        mailTextBuilder.addLine("No se realizaran busquedas o descargas multiples. Solo se tomara la primera que aparezca en el mensaje");

        mailTextBuilder.addLine();
        mailTextBuilder.addLine("== SERVICIOS DISPONIBLES ==");
        mailTextBuilder.addLine("- Revolico         Busque en el sitio de anuncios cubano");
        mailTextBuilder.addLine("- Download         Descargue un fichero pequeno a partir de una URL y recibalo como adjunto en su correo");
        mailTextBuilder.addLine("- StackOverflow    Para programadores! Aclare sus dudas en este sitio de preguntas y respuestas");

        mailTextBuilder.addLine();
        mailTextBuilder.addLine("== EJEMPLO ==");
        mailTextBuilder.addLine("Si desea buscar laptops gratis en revolico, haga un nuevo correo con estos datos.");
        mailTextBuilder.addLine("Para: {0}", mailAddr);
        mailTextBuilder.addLine("Asunto: Revolico");
        mailTextBuilder.addLine("Cuerpo del correo: Laptop gratis");
        mailTextBuilder.addLine();
        mailTextBuilder.addLine("Si desea descargar un fichero pequeno o una pagina y ya conoce la URL.");
        mailTextBuilder.addLine("Para: {0}", mailAddr);
        mailTextBuilder.addLine("Asunto: Download");
        mailTextBuilder.addLine("Cuerpo del correo: http://www.sitio.com/fichero.jpg");

        return mailTextBuilder.toString();
    }

    @Override
    public void processMessage(String searchText)
    {}

    @Override
    public String getMailSubject()
    {
        return "Ayuda";
    }

    @Override
    public String getMailText()
    {
        return getHelpText();
    }

    @Override
    public MailAttachment getMailAttachment()
    {
        return null;
    }


}
