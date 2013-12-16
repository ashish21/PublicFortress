package com.fort.project.neighbourhoodwatch.server;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
 


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.servlet.http.*;
// Notice that out of all imports we have used -none of them is a GAE class
@SuppressWarnings("serial")
public class MailServlet extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	String name = req.getParameter("name");
        String msg = req.getParameter("msg");
        String email = req.getParameter("email"); 
        String sub = req.getParameter("sub");
        String body=name+"<br/>"+email+"<br/>"+msg;
    	String recipientEmailAddress = "ashish21099@gmail.com";
                Properties props = new Properties();
                Session session = Session.getDefaultInstance(props, null);
                Message mssg = new MimeMessage(session);
                // We are creating multipart so that we can send multiple parts to Email
                                Multipart mp = new MimeMultipart();
                try {
                	
                    mssg.setFrom(new InternetAddress("ashish21099@gmail.com", name));
                    mssg.addRecipient(Message.RecipientType.TO, new InternetAddress(
                            recipientEmailAddress, "admin"));
                    mssg.setSubject(sub);
                    MimeBodyPart htmlPart = new MimeBodyPart();
                    htmlPart.setContent(body, "text/html");
                    // We are composing multipart with various parts of email, before we send
                    mp.addBodyPart(htmlPart);
                    mssg.setContent(mp);
                    Transport.send(mssg);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                resp.getWriter().println("Your response has been successfully recorded.");
 
    }
    
}