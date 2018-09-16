package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class WebAppController {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @RequestMapping("/us-companies")
    public List<Company> USCompanies() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("https://my.api.mockaroo.com/us-companies.json?key=e68417c0", List.class);
        List<Company> usCompanies = responseEntity.getBody();
        return usCompanies;
    }
}