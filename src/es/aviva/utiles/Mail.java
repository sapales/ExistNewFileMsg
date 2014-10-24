/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.aviva.utiles;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author m0072
 */
public class Mail {
        
    public static boolean EnviaMail(String host, String puerto, String mailFrom, String mailTo, String asunto, String cuerpo){

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        Session session = Session.getInstance(props, null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(mailFrom);
            msg.setRecipients(Message.RecipientType.TO, mailTo);
            msg.setSubject(asunto);
            msg.setText(cuerpo);
            
            Transport t = session.getTransport("smtp");
            t.connect();
            t.sendMessage(msg,msg.getAllRecipients());
            t.close();
            return true;
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
            return false;
        }
        
    }
    
}
