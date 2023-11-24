package com.havenlife.dnb.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.havenlife.dnb.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileFunctions {
    @Autowired
    private ObjectMapper objectMapper;
    public Path createFile(String fileName) {
        Path file = createFilePath(fileName);
        if (!Files.exists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    private Path createFilePath(String fileName) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        return currentPath.resolve("data").resolve(fileName);
    }

    public void writeToFile(String fileToWrite, String line) {
        Path path = createFile(fileToWrite);
        try {
            Files.writeString(path, line.trim() + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readFromFile(String fileName, int numberOfLinesToRead) {
        List<String> readLines = new ArrayList<>();
        try {
            Path path = createFilePath(fileName);

            if (numberOfLinesToRead == -1) {
                readLines = Files.readAllLines(path);
                return readLines;
            }
            FileReader fileReader = new FileReader(path.toFile());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (int i = 0; i < numberOfLinesToRead; i++) {

                String lineRead = bufferedReader.readLine();
                if (lineRead == null) {
                    break;
                }
                readLines.add(lineRead);
            }
            bufferedReader.close();
            return readLines;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteApplication(int id, String fileName) {
        try {
            List<String> applicationsWithRemovedId = new ArrayList<>();
            Path path = createFilePath(fileName);

            List<String> readAllLines = readFromFile(fileName, -1);
            objectMapper.registerModule(new JavaTimeModule());
            for (int i = 0; i < readAllLines.size(); i++) {
                Application app = objectMapper.readValue(readAllLines.get(i), Application.class);
                if (id != app.getId()) {
                    applicationsWithRemovedId.add(readAllLines.get(i));
                }
            }
            Files.deleteIfExists(path);
            applicationsWithRemovedId.forEach(line -> writeToFile(fileName, line));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateApplication(Application newApplication, String fileName) {
        List<String>listOfAllApplications = readFromFile(fileName, -1);
//        objectMapper.registerModule(new JavaTimeModule());
        for (int i = 0; i < listOfAllApplications.size(); i++) {
            try {
                Application appFromFile = objectMapper.readValue(listOfAllApplications.get(i), Application.class);
                if (appFromFile.getId() == newApplication.getId()) {
                    String appInString = objectMapper.writeValueAsString(newApplication);
                    listOfAllApplications.set(i, appInString );
                    break;
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        listOfAllApplications.forEach(line -> writeToFile(fileName, line));
    }
}
