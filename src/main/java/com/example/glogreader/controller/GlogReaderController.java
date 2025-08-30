package com.example.glogreader.controller;

import com.example.glogreader.util.PDFUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GlogReaderController {

    @GetMapping("/glogReader")
    public String glogReader() {
        // Just show the form
        return "glogReader"; // maps to webapp/glogReader.jsp
    }

    @PostMapping("/sendGlogReader")
    @ResponseBody
    public Map<String, Object> sendGlogReader(Model model,
                                              @RequestParam("glog") MultipartFile glog,
                                              @RequestParam("book") MultipartFile book) throws IOException {
        // Calls your existing PDFUtil logic
        PDFUtil.extractGlog(model, glog, book);

        Map<String, Object> response = new HashMap<>();

        response.put("message", model.getAttribute("message"));
        response.put("folder", model.getAttribute("folder"));

        // JSP page to render
        return response;
    }

    @PostMapping("/clearGlogReader")
    public String clearGlogReader(Model model) {
        // Clear all attributes for JSP
        model.addAttribute("message", "");
        model.addAttribute("glog", "");
        model.addAttribute("book", "");
        model.addAttribute("success", "");
        model.addAttribute("folder", null);

        return "glogReader";
    }
}