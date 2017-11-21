package rest.service.decreasingfrequency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class FrequencyController {

    @RequestMapping("/decreasing-frequency")
    public String getStringFrequencies(@RequestParam(value = "file") String fileName) {
        try {
            Map<String, Integer> frequencyMap = computeFrequencyMap(fileName);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(frequencyMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    private Map<String,Integer> computeFrequencyMap(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(fileName));
            String read;
            Map<String, Integer> unsortedMap = new HashMap<>();
            int value = 0;
            while ((read = bufferedReader.readLine()) != null) {
                String[] words = read.split("\\s");
                for (String s : words) {
                    s = s.replace(",", "");
                    if (unsortedMap.containsKey(s)) {
                        value = unsortedMap.get(s);
                    }
                    unsortedMap.put(s, value + 1);
                }
            }

            return unsortedMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
