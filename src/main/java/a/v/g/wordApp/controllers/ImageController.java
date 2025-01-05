package a.v.g.wordApp.controllers;

import a.v.g.wordApp.model.FileDB;
import a.v.g.wordApp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {
    @Autowired
    private FileStorageService storageService;

    //это контроллер для шаблона (а сам шаблон дергает метод из файла контроллера)
    @GetMapping("/view-image/{fileOid}")
    public String viewImage(@PathVariable String fileOid, Model model) {
        FileDB fileDB = storageService.getFile(fileOid).get();
        model.addAttribute("fileOid", fileOid);
        model.addAttribute("fileDB", fileDB);
        return "image-renderer"; // Имя Thymeleaf-шаблона
    }
}