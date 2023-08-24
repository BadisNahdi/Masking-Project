package com.badis.NER.Mask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MainController {

    @Autowired
    private entityMapRepository entityMapRepository;

    public String Mask(MultipartFile file, String checkbox1, String checkbox2, String checkbox3) {

        String fileContent;
        if (file.isEmpty()) {
            return "Error: Empty file.";
        }

        try {
            fileContent = FileToStringConverter.convert(file);

        } catch (IOException e) {
            return "Error GFGF: " + e.getMessage();
        }
        MaskedLog maskedLog = new MaskedLog(fileContent, entityMapRepository);

        if (!checkbox1.isEmpty()) {
            maskedLog.Mask_Names();
        }
        if (!checkbox2.isEmpty()) {
            maskedLog.Mask_Email();
        }
        if (!checkbox3.isEmpty()) {
            maskedLog.mask_PhysicalAddress();
        }
        return maskedLog.getMaskedMessage();
    }

    @PostMapping("/maskTest")
    public String handleUpload(
            @RequestParam("fileInput") MultipartFile file,
            @RequestParam(value = "checkbox1", required = false) String checkbox1,
            @RequestParam(value = "checkbox2", required = false) String checkbox2,
            @RequestParam(value = "checkbox3", required = false) String checkbox3) {
        return Mask(file, checkbox1, checkbox2, checkbox3);
    }

    @PostMapping("/DownloadText")
    public ResponseEntity<ByteArrayResource> downloadTextFile(
            @RequestParam("fileInput") MultipartFile file,
            @RequestParam(value = "checkbox1", required = false) String checkbox1,
            @RequestParam(value = "checkbox2", required = false) String checkbox2,
            @RequestParam(value = "checkbox3", required = false) String checkbox3) {
        String maskedText = Mask(file, checkbox1, checkbox2, checkbox3);
        byte[] maskedBytes = maskedText.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(maskedBytes);

        return ResponseEntity.ok()
                .contentLength(maskedBytes.length)
                .header("Content-Disposition", "attachment; filename=MaskedText.txt")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}