package email;

import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail
{
    public static boolean sendMail(String msg, String address) {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getInstance(props);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("xiong-yang@qq.com"));
            message.setSubject("系统邮件-找回密码");
            message.setContent("请点击以下链接找回密码, 30分钟以内有效!<br>" + msg, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();

            Transport transport = session.getTransport();
            transport.connect("smtp.qq.com", "xiong-yang@qq.com", "xy58316992");

            transport.sendMessage(message, new Address[]{new InternetAddress(address)});
            transport.close();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void main(String [] args) throws GeneralSecurityException, MessagingException {
        sendMail("asdfasdf", "xiong-yang@qq.com");
    }
}