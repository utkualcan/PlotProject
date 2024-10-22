package com.example.plotproject;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Controller
public class PlotController {
    @Value(value = "classpath:plot.R")
    private Resource rSource;

    private List<Double> values;
    private int currentIndex = 0;

    @Autowired
    private Context graalVMContext;

    private Function<DataHolder, String> plotFunction;

    @Bean
    public List<Double> loadCSVData() throws IOException {
        CSVReader csvReader = new CSVReader("/home/alcanutku/Desktop/Projects/PlotProject/src/main/resources/swe307_pro1.csv");
        values = csvReader.readColumn(12); // Column 13 is index 12
        return values;
    }

    @Bean
    public void initializePlotFunction() throws IOException {
        Source source = Source.newBuilder("R", rSource.getURL()).build();
        this.plotFunction = graalVMContext.eval(source).as(Function.class);
    }

    @RequestMapping(value = "/plot", produces = "image/svg+xml")
    public ResponseEntity<String> load() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Refresh", "1");

        String svg = "";
        if (currentIndex < values.size()) {
            synchronized (plotFunction) {
                svg = plotFunction.apply(new DataHolder(values.get(currentIndex)));
            }
            currentIndex++;
        } else {
            currentIndex = 0;
        }

        return new ResponseEntity<>(svg, responseHeaders, HttpStatus.OK);
    }

    @Bean
    public Context getGraalVMContext() {
        return Context.newBuilder().allowAllAccess(true).build();
    }
}

