package rs.ac.uns.ftn.bsep.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import rs.ac.uns.ftn.bsep.domain.users.User;

@Component
public class EmailSender {


    @Autowired
    private JavaMailSender emailSender;

    public void sendForgotPasswordEmail(String id,String mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        String body = String.format("Please click on the link to reset your password: http://localhost:8081/#/reset/"+id);
        message.setTo(mail);
        message.setSubject("Forgot password");
        message.setText(body);
        emailSender.send(message);
    }
}
