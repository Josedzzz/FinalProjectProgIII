package co.edu.uniquindio.agencia.utilities;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class EmailUtils {

    public static void enviarEmail(String correo, String asunto, String contenido){

        String host = "smtp.gmail.com";
        final String user = "hermesagenciaoficial10@gmail.com";
        final String password = "qphnwqidzrljxjyi"; //ContraseÃ±a de aplicacion creada desde la seguridad de gmail

        String to= correo;

        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host",host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress( correo ));
            message.setSubject( asunto );
            message.setText( contenido );

            //send the message
            Transport.send(message);

            System.out.println("Mensaje enviado correctamente...");

        } catch (MessagingException e) {e.printStackTrace();}

    }
}
