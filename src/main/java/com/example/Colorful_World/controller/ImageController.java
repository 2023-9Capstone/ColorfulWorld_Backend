package com.example.Colorful_World.controller;

import com.example.Colorful_World.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image")
    @ResponseBody
    public ResponseEntity<String> imageSave(@RequestPart("image")MultipartFile img){

        imageService.saveImage(img);

        return ResponseEntity.ok("이미지 저장에 성공하였습니다.");
    }

    //저장된 이미지 확인
    @GetMapping("/load")
    public String loadImage(@RequestParam("id") int id, Model model){

        model.addAttribute("image", imageService.loadImage(id));

        return "image";
    }
}