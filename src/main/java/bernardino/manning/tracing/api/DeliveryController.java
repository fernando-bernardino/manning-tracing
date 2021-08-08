package bernardino.manning.tracing.api;

import bernardino.manning.tracing.client.LogisticClient;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/arrangeDelivery")
public class DeliveryController {

    private final JaegerTracer tracer;
    private final LogisticClient logisticClient;

    public DeliveryController(JaegerTracer jaegerTracer, LogisticClient logisticClient) {
        this.tracer = jaegerTracer;
        this.logisticClient = logisticClient;
    }

    @PostMapping
    public ResponseEntity<?> arrangeDelivery() {
        Span span = tracer.buildSpan("delivery").start();
        try (Scope scope = tracer.scopeManager().activate(span)){
            logisticClient.transport();

            return ResponseEntity.ok("Delivery was arranged.");
        } catch(Exception ex) {
            Tags.ERROR.set(span, true);
            span.log(Map.of(Fields.EVENT, "error", Fields.ERROR_OBJECT, ex, Fields.MESSAGE, ex.getMessage()));

            return ResponseEntity.internalServerError()
                    .body("Something went wrong, please contact our customer service.");
        } finally {
            span.finish();
        }
    }
}
