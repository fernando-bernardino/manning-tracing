package bernardino.manning.tracing.billing;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Span;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private final JaegerTracer tracer;

    public BillingService(JaegerTracer tracer) {
        this.tracer = tracer;
    }

    public void payment(Span span) {
        Span child = tracer.buildSpan("payment").asChildOf(span).start();
        try {
            //  do something ...
            child.log("payment done");
        } finally {
            child.finish();
        }
    }
}
