package bernardino.manning.tracing.logistics;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    private final LogisticsService logisticsService;

    private final JaegerTracer tracer;

    public DeliveryService(LogisticsService logisticsService, JaegerTracer jaegerTracer) {
        this.logisticsService = logisticsService;
        this.tracer = jaegerTracer;
    }

    public void arrangeDelivery() {
        Span span = tracer.buildSpan("arrangeDelivery").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            logisticsService.transport();

            span.log("done arranging delivery");
        } finally {
            span.finish();
        }
    }
}
