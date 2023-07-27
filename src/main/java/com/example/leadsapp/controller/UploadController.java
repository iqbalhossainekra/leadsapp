package com.example.leadsapp.controller;

import com.example.leadsapp.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class UploadController {

    private final LeadService leadService;

    @Autowired
    public UploadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/error";
        }

        try {

            String filePath = "/path/to/temp/" + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            leadService.uploadLeadsFromCsv(filePath);

            return "redirect:/success";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}
