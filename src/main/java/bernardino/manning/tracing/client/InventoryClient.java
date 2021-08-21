package bernardino.manning.tracing.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;

@Component
public class InventoryClient {

    private final JaegerAwareRestTemplate restTemplate;

    private URI baseUri;

    public InventoryClient(JaegerAwareRestTemplate restTemplate, @Value("${inventory.url:http://inventory:8080/}") String baseUrl)
            throws URISyntaxException {
        this.restTemplate = restTemplate;
        this.baseUri = new URI(baseUrl);
    }

    public void createOrder() {
        URI paymentUri = baseUri.resolve("createOrder");
        ResponseEntity<?> response = restTemplate.postForEntity(paymentUri, null, Void.class);

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(format("Called failed: code {}, body {}",
                    response.getStatusCodeValue(), response.getBody()));
        }
    }
}
