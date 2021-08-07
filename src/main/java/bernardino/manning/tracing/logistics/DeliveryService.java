package bernardino.manning.tracing.logistics;

import io.jaegertracing.internal.JaegerTracer;
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

    public void arrangeDelivery(Span span) {
        Span child = tracer.buildSpan("arrangeDelivery").asChildOf(span).start();
        try {
            logisticsService.transport(child);

            child.log("done arranging delivery");
        } finally {
            child.finish();
        }
    }
}
