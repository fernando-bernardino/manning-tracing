package bernardino.manning.tracing.api;

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
@RequestMapping("/createOrder")
public class InventoryController {

    @PostMapping
    public ResponseEntity<?> arrangeDelivery(@RequestHeader Map<String, String> headers) {
        try {
            return ResponseEntity.ok("Order was created.");
        } catch(Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Something went wrong, please contact our customer service.");
        }
    }
}
