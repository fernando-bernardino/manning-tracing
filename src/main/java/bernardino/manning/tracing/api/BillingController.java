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
@RequestMapping("/payment")
public class BillingController {

    private final JaegerTracer tracer;
    private final JaegerContextExtractor extractor;

    public BillingController(JaegerTracer tracer, JaegerContextExtractor extractor) {
        this.tracer = tracer;
        this.extractor = extractor;
    }

    @PostMapping
    public ResponseEntity<?> doPayment(@RequestHeader Map<String, String> headers) {
        Span span = extractor.createSpan("billing", headers);
        try (Scope scope = tracer.scopeManager().activate(span)){
            return ResponseEntity.ok("You have successfully payed your order.");
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