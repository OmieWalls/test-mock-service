package app;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class WebAppController {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @RequestMapping("/us-companies")
    public List<LinkedHashMap<String, Object>> USCompanies() {

        var restTemplate = new RestTemplate();
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("https://my.api.mockaroo.com/us-companies.json?key=e68417c0", List.class);
        List<LinkedHashMap<String, Object>> usCompanies = responseEntity.getBody();
        Storage storage = StorageOptions.getDefaultInstance().getService();

        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
        var ps = new PubSubController();
        ps.publishMessage(usCompanies);
        return usCompanies;
    }
}