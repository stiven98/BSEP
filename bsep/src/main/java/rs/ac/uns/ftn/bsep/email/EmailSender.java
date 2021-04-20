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

    public void sendForgotPasswordEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        String body = String.format("Please click on the link to reset your password: url");
        message.setTo("jovanbosnic7@gmail.com");
        message.setSubject("Forgot password");
        message.setText(body);
        emailSender.send(message);
    }
}
