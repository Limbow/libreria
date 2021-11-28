/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.controladores;


import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Files;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Joaquin
 */
@Controller
public class VideoController {

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attribute) throws IOException {

        if (file == null || file.isEmpty()) {
            attribute.addFlashAttribute("text", "El archivo no puede ser nulo");
            return "/error";
        }

        StringBuilder builder = new StringBuilder();

        builder.append(System.getProperty("user.home"));
        builder.append(File.separator);
        builder.append("springvideos");
        builder.append(File.separator);
        builder.append(file.getOriginalFilename());

        byte[] fileBytes = file.getBytes();
        Path path = Paths.get(builder.toString());
        Files.write(path, fileBytes);
        
        attribute.addFlashAttribute("titulo", "El archivo se cargo exitosamente ["+file.getOriginalFilename()+"]");
        return "succes.html";
    }
}
