package bernardino.manning.tracing.billing;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private final JaegerTracer tracer;

    public BillingService(JaegerTracer tracer) {
        this.tracer = tracer;
    }

    public void payment() {
        Span span = tracer.buildSpan("payment").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            //  do something ...
            span.log("payment done");
        } finally {
            span.finish();
        }
    }
}
