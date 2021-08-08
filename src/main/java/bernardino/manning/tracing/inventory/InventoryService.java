package bernardino.manning.tracing.inventory;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final JaegerTracer tracer;

    public InventoryService(JaegerTracer tracer) {
        this.tracer = tracer;
    }

    public void createOrder() {
        Span span = tracer.buildSpan("createOrder").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            //  do something ...
            span.log("done creating order");
        } finally {
            span.finish();
        }
    }
}
