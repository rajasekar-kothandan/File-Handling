package com.poc.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FileProcessorTest {
    @InjectMocks
    private FileProcessor fileProcessor;

    String basePath = "C:\\Users\\DELL\\Desktop";
    String testsettings = null;
    String processFolder_1 = basePath + "\\File_POC";

    @BeforeEach
    void setup() {
        log.info("Entering into FileProcessorTest::setup");
        try {
            testsettings = "testsettings.txt";
            File testsettingsFilePath = new File(basePath + File.separator + testsettings);
            if (testsettingsFilePath.exists()) {
                Scanner sc = new Scanner(testsettingsFilePath);
                while (sc.hasNextLine()) {
                    String data = sc.nextLine();
                    if (data.contains(processFolder_1)) {
                        continue;
                    } else {
                        FileWriter test_settings_fw = new FileWriter(testsettingsFilePath, true); //the true will append the new data
                        test_settings_fw.write(processFolder_1 + "\n");//appends the string to the file
                        test_settings_fw.close();
                    }
                }
            } else {
                FileWriter test_settings_fw = new FileWriter(testsettingsFilePath, true); //the true will append the new data
                test_settings_fw.write(processFolder_1 + "\n");//appends the string to the file
                test_settings_fw.close();
            }

            if (new File(processFolder_1).exists()) {
                log.info("Base Directory is found");
            } else {
                new File(processFolder_1).mkdirs();
            }

            String sample1 = "sample1.txt";
            File sampleFile = new File(processFolder_1 + File.separator + sample1);
//            String sample1 = basePath+"\\sample1.txt";
            FileWriter sample1_fw = new FileWriter(sampleFile, true); //the true will append the new data
            sample1_fw.write("Hi,\nHow Are you?\nI'm good and how about you?\nNice to hear.");//appends the string to the file
            sample1_fw.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("Exiting from FileProcessorTest::setup");
    }

    @Test
    void runMethodTest() {
        if (null != testsettings) {
            fileProcessor.run(testsettings);
        }
    }

}
