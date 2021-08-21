package bernardino.manning.tracing.api;

import io.jaegertracing.internal.JaegerSpan;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapAdapter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JaegerContextExtractor {

    private final JaegerTracer tracer;

    public JaegerContextExtractor(JaegerTracer tracer) {
        this.tracer = tracer;
    }

    public JaegerSpan createSpan(String operationName, Map<String, String> headers) {
        SpanContext spanContext = tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapAdapter(headers));
        if (spanContext == null) {
            return tracer.buildSpan(operationName).start();
        } else {
            return tracer.buildSpan(operationName).asChildOf(spanContext).start();
        }
    }
}
