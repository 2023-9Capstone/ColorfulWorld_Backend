package com.example.Colorful_World.controller;


import com.example.Colorful_World.dto.LoginDto;
import com.example.Colorful_World.dto.UserDto;
import com.example.Colorful_World.service.MailService;
import com.example.Colorful_World.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletResponse response;

    @PostMapping("/api/join")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody Map<String, String> param){

        UserDto userDto = new UserDto(param.get("email"),
                passwordEncoder.encode(param.get("password")),
                Integer.parseInt(param.get("intensity")));


        userService.register(userDto);

        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }


    @GetMapping("/api/checkEmail")
    @ResponseBody
    public ResponseEntity<Object> checkEmail(@RequestParam("email") String email){

        String code = mailService.sendMail(email);

        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<Object> login(@RequestBody Map<String,String> param){

        LoginDto loginDto = new LoginDto(param.get("email"),
                param.get("password"));

        userService.login(loginDto, response);

        return new ResponseEntity<>("로그인에 성공하였습니다.", HttpStatus.OK);
    }

    @PostMapping("/api/out")
    @ResponseBody
    public ResponseEntity<Object> logout(@RequestHeader("access_token") String atk){

        userService.logout(atk);

        return new ResponseEntity<>("로그아웃에 성공하였습니다.", HttpStatus.OK);
    }

    @PostMapping("/api/update")
    @ResponseBody
    public ResponseEntity<Object> updateIntensity(@RequestHeader("access_token") String atk,
                                                  @RequestBody Map<String, String> param){

        int intensity = Integer.parseInt(param.get("intensity"));

        userService.updateIntensity(atk, intensity, response);

        return new ResponseEntity<>("인데스 수정이 완료되었습니다.", HttpStatus.OK);
    }
}
