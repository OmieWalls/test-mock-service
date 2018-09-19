package app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class WebAppController {

    @RequestMapping("/data")
    public List<LinkedHashMap<String, Object>> publishData(@RequestParam("schema") String schema,
                                                           @RequestParam("projectId") String projectId,
                                                           @RequestParam("mockarooApiKey") String mockarooApiKey) {

        var restTemplate = new RestTemplate();
        String url = "https://my.api.mockaroo.com/" + schema.toLowerCase() + ".json?key=" + mockarooApiKey;
        ResponseEntity<List> responseEntity = restTemplate.getForEntity(url, List.class);
        List<LinkedHashMap<String, Object>> data = responseEntity.getBody();

        var ps = new PubSubController();
        ps.publishGeneratedData(data, schema.replace("-",""), projectId);
        return data;
    }
}