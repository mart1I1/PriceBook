package io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private File file;

    public FileManager(String filename) throws IOException {
        this.file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void removeData(String data) throws IOException {
        File temp = new File("temp.txt");
        if (!temp.createNewFile()) {
            System.out.println("temp not create");
        }

        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(temp));

        String line;
        while ((line = fileReader.readLine()) != null) {
            if (!line.equals(data)) {
                fileWriter.append(line);
                fileWriter.newLine();
            }
        }
        fileReader.close();
        fileWriter.close();

        file.delete();
        temp.renameTo(file);
    }

    public void addData(String data) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.append(data).append("\n");
        fileWriter.close();
    }

    public List<String> getAllData() throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line;
        List<String> lines = new ArrayList<>();
        while ((line = fileReader.readLine()) != null ) {
            lines.add(line);
        }
        fileReader.close();
        return lines;
    }

    public void editData(String from, String to) throws IOException {
        File temp = new File("temp.txt");
        if (!temp.createNewFile()) {
            System.out.println("temp not create");
        }

        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(temp));

        String line;
        while ((line = fileReader.readLine()) != null) {
            if (!line.equals(from)) {
                fileWriter.append(line);
            } else {
                fileWriter.append(to);
            }
            fileWriter.newLine();
        }
        fileReader.close();
        fileWriter.close();

        file.delete();
        temp.renameTo(file);

    }
}
