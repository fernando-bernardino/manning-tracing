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
@RequestMapping("/transport")
public class LogisticController {

    private final JaegerTracer tracer;
    private final JaegerContextExtractor extractor;

    public LogisticController(JaegerTracer tracer, JaegerContextExtractor extractor) {
        this.tracer = tracer;
        this.extractor = extractor;
    }

    @PostMapping
    public ResponseEntity<?> arrangeDelivery(@RequestHeader Map<String, String> headers) {
        Span span = extractor.createSpan("transport", headers);
        try (Scope scope = tracer.scopeManager().activate(span)){
            return ResponseEntity.ok("Transport was arranged");
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
