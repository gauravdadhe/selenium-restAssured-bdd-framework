package candidatex.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReportMerger {
    static final Logger logger = Logger.getLogger(TestProperties.class);
    static List<String> featureNames = new ArrayList<>();
    static JSONParser parser = new JSONParser();
    static JSONArray originalResults = new JSONArray();
    static Integer fileName = 1;

    public static void main(String[] args) throws ParseException, IOException {
        File folder = new File("target/cucumber-report");
        listAllFiles(folder);
        getFeatureList();
        buildFeatureReportFile();

    }

    // Uses listFiles method
    public static void listAllFiles(File folder) throws ParseException {
        File[] fileNames = folder.listFiles();
        for (File file : fileNames) {
            // if directory call the same method again
            if (file.isDirectory()) {
                listAllFiles(file);
            } else {
                try {
                    readContent(file);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void readContent(File file) throws IOException, ParseException {
        logger.debug("processing file " + file.getCanonicalPath());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            Object obj = parser.parse(jsonString.toString());
            JSONArray temp = (JSONArray) obj;
            temp.forEach(item -> {
                try {
                    JSONObject scenario = (JSONObject) item;
                    originalResults.add(scenario);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            logger.debug(e.getMessage());

        }
    }

    public static void getFeatureList() {
        originalResults.forEach(item -> {
            JSONObject scenario = (JSONObject) item;
            String id = scenario.get("id").toString();
            if (!featureNames.contains(id)) {
                featureNames.add(id);
            }

        });
    }

    public static void buildFeatureReportFile() throws IOException {

        JSONObject featureResults = new JSONObject();
        JSONArray featureResultArray = new JSONArray();
        JSONArray scenarioElements = new JSONArray();
        HashMap<String, String> featureDetails = new HashMap<String, String>();
        HashMap<String, JSONArray> featureTags = new HashMap<String, JSONArray>();

        for (String featureName : featureNames) {

            originalResults.forEach(item -> {
                JSONObject scenario = (JSONObject) item;

                String id = scenario.get("id").toString();
                String name = scenario.get("name").toString();
                String description = scenario.get("description").toString();
                String keyword = scenario.get("keyword").toString();
                String uri = scenario.get("uri").toString();
                String line = scenario.get("line").toString();
                String tagString = scenario.get("tags").toString();
                JSONArray tags = new JSONArray();

                try {
                    tags = (JSONArray) parser.parse(tagString);
                } catch (Exception e) {
                }

                if (id.equals(featureName)) {
                    JSONArray elements;
                    try {
                        elements = (JSONArray) parser.parse(scenario.get("elements").toString());
                        elements.forEach(element -> {
                            scenarioElements.add(element);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    featureDetails.put("id", id);
                    featureDetails.put("name", name);
                    featureDetails.put("description", description);
                    featureDetails.put("keyword", keyword);
                    featureDetails.put("uri", uri);
                    featureDetails.put("line", line);
                    featureTags.put("tags", tags);

                }
            });
            featureResults.putAll(featureDetails);
            featureResults.putAll(featureTags);
            featureResults.put("elements", scenarioElements);
            featureResultArray.add(featureResults);
            String finalResult = featureResultArray.toJSONString();
            bufferedWritter(finalResult, fileName.toString());
            featureResultArray.clear();
            scenarioElements.clear();
            fileName++;
        }

    }

    public static void bufferedWritter(String fileContent, String fileName) throws IOException {
        String dirPath = "./target/aggregatedResults/";
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath + fileName + ".json"));
        writer.write(fileContent);
        writer.close();
    }
}
