package bernardino.manning.tracing.api;

import bernardino.manning.tracing.client.LogisticClient;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/arrangeDelivery")
public class DeliveryController {
    private final LogisticClient logisticClient;

    public DeliveryController(LogisticClient logisticClient) {
        this.logisticClient = logisticClient;
    }

    @PostMapping
    public ResponseEntity<?> arrangeDelivery(@RequestHeader Map<String, String> headers) {
        try {
            logisticClient.transport();

            return ResponseEntity.ok("Delivery was arranged.");
        } catch(Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Something went wrong, please contact our customer service.");
        }
    }
}
