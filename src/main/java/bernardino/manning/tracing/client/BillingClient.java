package bernardino.manning.tracing.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;

@Component
public class BillingClient {
    private final RestTemplate restTemplate;
    private URI baseUri;

    public BillingClient(RestTemplate restTemplate, @Value("${billing.url:http://billing:8080/}") String baseUrl)
            throws URISyntaxException {
        this.restTemplate = restTemplate;
        this.baseUri = new URI(baseUrl);
    }

    public void doPayment() {
        URI paymentUri = baseUri.resolve("payment");
        ResponseEntity<?> response = restTemplate.postForEntity(paymentUri, null, Void.class);

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(format("Called failed: code {}, body {}",
                    response.getStatusCodeValue(), response.getBody()));
        }
    }
}
