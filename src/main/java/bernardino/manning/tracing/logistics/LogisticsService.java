package bernardino.manning.tracing.logistics;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import org.springframework.stereotype.Service;

@Service
public class LogisticsService {

    private final JaegerTracer tracer;

    public LogisticsService(JaegerTracer tracer) {
        this.tracer = tracer;
    }

    public void transport(Span span) {
        Span child = tracer.buildSpan("transport").asChildOf(span).start();
        try {
            //  do something ...
            child.log("transportation arranged");
        } finally {
            child.finish();
        }
    }
}
