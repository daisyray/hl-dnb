package com.havenlife.dnb.controllers;

import com.havenlife.dnb.models.Output;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.PathContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/static")
public class StaticController {
    @Value("${is-development}") Boolean isDev;

    @GetMapping("/{release}/{*files}")
    public ResponseEntity<byte[]> readStaticFile(
        @PathVariable("release") String release,
        @PathVariable("files") String filepaths
    ) {
        try {
            var filenames = Arrays.stream(filepaths.trim().split("/")).map(String::trim).toList();
            if (filenames.isEmpty()) {
                throw new IllegalArgumentException("files are not present");
            }
            Output output = null;
            if (isDev) {
                var workingFolder = Paths.get(System.getProperty("user.dir")).resolve("src/main/resources/static");
                for (var filename : filenames) {
                    workingFolder = workingFolder.resolve(filename);
                }
                output = new Output(Files.readAllBytes(workingFolder), Map.of(HttpHeaders.CACHE_CONTROL, "no-store"));
            } else {
                var fileName = String.join("/", filenames);
                var file = "/static/" + fileName;
                try (var resource = new ClassPathResource(file, this.getClass().getClassLoader()).getInputStream()) {
                    output = new Output(
                        resource.readAllBytes(),
                        Map.of(HttpHeaders.CACHE_CONTROL, "max-age=31536000" )
                    );
                }
            }
            var builder = ResponseEntity.status(HttpStatus.OK)
                   .header(HttpHeaders.CONTENT_LENGTH, output.getBytes().length + "")
                   .header(HttpHeaders.CONTENT_TYPE, mimeType(filenames.get(filenames.size()-1)));

            for (var entry: output.getHeaders().entrySet()) {
                builder.header(entry.getKey(), entry.getValue().toString());
            }
            return builder.body(output.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String mimeType(String filename) {
        var extension = FilenameUtils.getExtension(filename);
        return switch (extension) {
            case "html" -> MediaType.TEXT_HTML_VALUE;
            case "css" ->  "text/css";
            case "png" -> "image/png";
            case "jpg","jpeg" -> "image/jpeg";
            case "js" -> "text/javascript";
            case "json" -> MediaType.APPLICATION_JSON_VALUE;
            case "xml" -> MediaType.TEXT_XML_VALUE;
            case "pdf" -> "application/pdf";
            case "csv" -> "text/csv";
            case "ico" -> "image/x-icon";
            case "woff" -> "font/woff";
            case "woff2" -> "font/woff2";
            default -> throw new IllegalArgumentException("Unknown file Extension " + extension);
        };
    }
}
