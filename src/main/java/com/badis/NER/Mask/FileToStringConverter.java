package com.badis.NER.Mask;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class FileToStringConverter {

    public static String convert(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public static File createTextFile(String content, String fileName) throws IOException {
        File outputFile = new File(fileName);
        System.out.println("BAYYYYTEECH");

        try (Writer writer = new BufferedWriter(new FileWriter(outputFile))) {
            System.out.println("ENFIN");
            writer.write(content);
        }

        return outputFile;
    }
}