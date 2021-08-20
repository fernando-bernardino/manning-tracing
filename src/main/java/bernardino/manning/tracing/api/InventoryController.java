package bernardino.manning.tracing.api;

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
@RequestMapping("/createOrder")
public class InventoryController {

    private final JaegerTracer tracer;

    public InventoryController(JaegerTracer tracer) {
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<?> arrangeDelivery() {
        Span span = tracer.buildSpan("inventory").start();
        try (Scope scope = tracer.scopeManager().activate(span)){
            return ResponseEntity.ok("Order was created.");
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
