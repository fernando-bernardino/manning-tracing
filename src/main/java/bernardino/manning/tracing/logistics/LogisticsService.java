package bernardino.manning.tracing.logistics;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.springframework.stereotype.Service;

@Service
public class LogisticsService {

    private final JaegerTracer tracer;

    public LogisticsService(JaegerTracer tracer) {
        this.tracer = tracer;
    }

    public void transport() {
        Span span = tracer.buildSpan("transport").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            //  do something ...
            span.log("transportation arranged");
        } finally {
            span.finish();
        }
    }
}
