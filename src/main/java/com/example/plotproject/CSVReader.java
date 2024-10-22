package com.example.plotproject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private final String path;

    public CSVReader(String path) {
        this.path = path;
    }

    public List<Double> readColumn(int column) throws IOException {
        List<Double> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String valueStr = record.get(column + 1);
                try {
                    values.add(Double.parseDouble(valueStr));
                } catch (NumberFormatException e) {
                    System.err.println("Geçersiz sayı: " + valueStr);
                }
            }
        }
        return values;
    }
}
