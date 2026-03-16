/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Yuikiri
 */
public class SenMailUtil {
    public static void sendOTP(String toEmail, String otpCode) {
        // ĐIỀN THÔNG TIN THẬT CỦA SẾP VÀO ĐÂY:
        final String fromEmail = "phamquochoang356@gmail.com"; 
        final String password = "ppar vdxn ltdv bcwp"; 

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Verify code - MediCare");
            
            String htmlContent = "<h3>Yêu cầu thay đổi thông tin</h3>"
                    + "<p>Mã OTP của bạn là: <b style='color:red; font-size: 24px;'>" + otpCode + "</b></p>"
                    + "<p>Mã này có hiệu lực 5 phút. KHÔNG chia sẻ cho bất kỳ ai.</p>";
            
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
