package edu.virginia.sde.reviews;

import org.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Collectors;

public class Configuration {
    private final String configurationFile = "config.json";

    private String fileNameDatabase;

    public String getFileNameDatabase() {
        if(fileNameDatabase == null) {
            parseJsonConigFile();
        }
        return fileNameDatabase;
    }

    public void parseJsonConigFile() {
        try (InputStream inputStream = Objects.requireNonNull(Configuration.class.getResourceAsStream(configurationFile));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            var jsonContent = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            var jsonConfig = new JSONObject(jsonContent);
            fileNameDatabase = jsonConfig.get("database").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
