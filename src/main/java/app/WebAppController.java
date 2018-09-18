package app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class WebAppController {

    @RequestMapping("/us-companies")
    public List<LinkedHashMap<String, Object>> USCompanies() {

        var restTemplate = new RestTemplate();
        String url = "https://my.api.mockaroo.com/us-companies.json?key=" + System.getenv("MOCKAROO_KEY");
        ResponseEntity<List> responseEntity = restTemplate.getForEntity(url, List.class);
        List<LinkedHashMap<String, Object>> data = responseEntity.getBody();

        var ps = new PubSubController();
        ps.publishGeneratedData(data);
        return data;
    }
}