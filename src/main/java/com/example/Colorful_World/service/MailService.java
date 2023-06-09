package com.example.Colorful_World.service;

import com.example.Colorful_World.exception.BaseException;
import com.example.Colorful_World.exception.ErrorCode;
import com.example.Colorful_World.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final SpringTemplateEngine templateEngine;
    private final MailProperties mailProperties;

    public String sendMail(String email){

        //아이디 중복 확인
        if(userRepository.existsByEmail(email)){
            //중복 시 error message 넘겨줘야함
            throw new BaseException(ErrorCode.DUPLICATE_EMAIL);
        }

        String code = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[Colorful World] 회원가입을 위한 이메일 인증");

            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("code", code);

            String html = templateEngine.process("email", context);
            mimeMessageHelper.setText(html, true);

            javaMailSender.send(mimeMessage);

            return code;

        }catch(Exception e){
            throw new BaseException(ErrorCode.MAIL_NOT_SENT);
        }
    }


    public String createCode(){
        Random random = new Random();
        StringBuffer code = new StringBuffer();

        for(int i = 0; i < 6; i++){ //6자리로 생성(대문자와 숫자 조합)
            int index = random.nextInt(2);

            switch (index) {
                case 0:
                    code.append((char) ((int) (random.nextInt(26)) + 65)); //A~Z
                    break;
                case 1:
                    code.append((random.nextInt(10))); //0~9
                    break;
            }
        }
        return code.toString();
    }
}
