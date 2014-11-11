package kuger.loganalyzer.ui.environment;

import com.google.gson.*;
import kuger.loganalyzer.LogAnalyzerException;
import kuger.loganalyzer.ui.widgets.filter.FilterDto;
import kuger.loganalyzer.ui.widgets.filter.FilterType;

import java.io.*;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ConfigFileHandler {

    private ConfigFileHandler() {
    }

    protected static void save(CreateEnvironmentControllerDto dto, File configFile) {
        Gson gson = new Gson();
        String json = gson.toJson(dto);
        System.out.println(json);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static CreateEnvironmentControllerDto load(File file) throws LogAnalyzerException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(FilterDto.class, (JsonDeserializer<FilterDto>) (json, typeOfT, context) -> {
            JsonPrimitive type = json.getAsJsonObject().getAsJsonPrimitive("type");
            FilterType filterType = context.deserialize(type, FilterType.class);
            Class<? extends FilterDto> implementation = filterType.getDtoType();
            return new Gson().fromJson(json, implementation);
        });
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(line, CreateEnvironmentControllerDto.class);
        } catch (IOException e) {
            throw new LogAnalyzerException("Could not load config.", e);
        }
    }


}
