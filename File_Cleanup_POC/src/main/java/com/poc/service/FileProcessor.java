package com.poc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
@Service
public class FileProcessor implements CommandLineRunner {

    boolean flag = false;
    File basePath;
    String fileType;
    int count = 1;

    @Override
    public void run(String... args) throws RuntimeException {
        System.out.println("********** Application Started **********");
        // If you want to see Debug logs then we have to use logging.level.root=DEBUG
        log.debug("********** Application Started with Debug Logs **********");
        if (args.length == 1) {
            File file = new File(args[0]);
            readSettingsFile(file);
        } else {
            System.out.println("\n" + "Incorrect number of arguments");
            System.out.println("""

                    Exiting....
                    """);
            System.exit(1);
        }
        System.out.println("********** Application Completed **********");
    }

    public void readSettingsFile(File file) {
        log.debug("Entering into FileProcessor::readSettingsFile");
        try {
            int i = 0;
            String[] fileArgs = new String[10];
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                fileArgs[i] = sc.nextLine();
                log.debug("fileArgs[{}] = {}", i, fileArgs[i]);
                System.out.println("\n Started processing of Root Path: " + fileArgs[i]);
                basePath = new File(fileArgs[i]);
                for (int r = 0; r < count; r++) {
                    processing(basePath);
                    r++;
                }
                i++;
            }
        } catch (FileNotFoundException fEx) {
            System.out.println("\n unable to load file from path: " + file);
            throw new RuntimeException(fEx);
        } catch (ArrayIndexOutOfBoundsException aEx) {
            System.out.println("\n Allowed no of lines to load file from path: 100 " + file);
            throw new RuntimeException(aEx);
        }
        log.debug("Existing from FileProcessor::readSettingsFile");
    }

    private void processing(File rootPath) {
        log.debug("Entering into FileProcessor::processing");
        String[] fileObject = rootPath.list();
        int rootPathSize = Objects.requireNonNull(rootPath.list()).length;
        if (rootPathSize > 0) {
            processItems(rootPath, rootPathSize, fileObject);
        } else {
            System.out.println("********** Don't have file in given path**********: " + rootPath);
        }
        log.debug("Existing from FileProcessor::processing");
    }

    private void processItems(File rootPath, int rootPathSize, String[] fileObject) {
        log.debug("Entering into FileProcessor::processItems");
//        for (String item : fileObject) {
        for (int j = 0; j < rootPathSize; j++) {
            String item = fileObject[j];
            File filePath = new File(rootPath + File.separator + item);
            setFileType(filePath);
            switch (fileType) {
                case "File" -> processFile(filePath);
                case "Directory" -> {
                    count++;
                    processFolder(filePath);
                }
                default -> {
                }
            }
//            item = fileObject[j+1];
        }
        log.debug("Existing from FileProcessor::processItems");
    }

    private void processFile(File filePath) {
        log.debug("Entering into FileProcessor::processFile");
        removeFile(filePath);
        log.debug("Existing from FileProcessor::processFile");
    }

    private void processFolder(File folderPath) {
        log.debug("Entering into FileProcessor::processFolder");
        if (Objects.requireNonNull(folderPath.list()).length == 0) {
            removeFolder(folderPath);
            if (flag) {
                String newPath = String.valueOf(folderPath);
                if (newPath.contains("\\")) {
                    int pathSize = newPath.lastIndexOf("\\");
                    newPath = newPath.substring(0, pathSize);
                    processing(new File(newPath));
                }
            }
        } else {
            log.debug("Folder is not Empty");
            processing(folderPath);
        }
        log.debug("Existing from FileProcessor::processFolder");
    }

    private void removeFile(File deleteFilePath) {
        log.debug("Entering into FileProcessor::removeFile");
        if (deleteFilePath.delete()) {
            flag = true;
            System.out.println("Deleted File: " + deleteFilePath);
        } else {
            flag = false;
            System.out.println("********** Skipping File **********");
            System.out.println("This File is using in another program: " + deleteFilePath);
        }
        log.debug("Existing from FileProcessor::removeFile");
    }

    private void removeFolder(File deleteFolderPath) {
        log.debug("Entering into FileProcessor::removeFolder");
        if (deleteFolderPath.delete()) {
            flag = true;
            System.out.println("Deleted Folder: " + deleteFolderPath);
        } else {
            flag = false;
            System.out.println("********** Skipping Folder **********");
            System.out.println("This folder is using in another program: " + deleteFolderPath);
        }
        log.debug("Existing from FileProcessor::removeFolder");
    }

    private void setFileType(File filePath) {
        log.debug("Entering into FileProcessor::setFileType");
        if (filePath.isFile()) {
            log.debug("Type of processing Object id: File");
            fileType = "File";
        } else if (filePath.isDirectory()) {
            log.debug("Type of processing Object id: Folder");
            fileType = "Directory";
        }
        log.debug("Existing from FileProcessor::setFileType");
    }

}