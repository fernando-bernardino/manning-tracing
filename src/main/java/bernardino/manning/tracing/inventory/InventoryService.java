package bernardino.manning.tracing.inventory;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final JaegerTracer tracer;

    public InventoryService(JaegerTracer tracer) {
        this.tracer = tracer;
    }

    public void createOrder(Span span) {
        Span child = tracer.buildSpan("createOrder")
                .asChildOf(span)
                .start();
        try {
            //  do something ...
            child.log("done creating order");
        } finally {
            child.finish();
        }
    }
}
