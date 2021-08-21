package bernardino.manning.tracing.client;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapAdapter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class JaegerAwareRestTemplate {

    private final JaegerTracer tracer;
    private final RestTemplate restTemplate;

    public JaegerAwareRestTemplate(JaegerTracer tracer, RestTemplate restTemplate) {
        this.tracer = tracer;
        this.restTemplate = restTemplate;
    }

    public <B,R> ResponseEntity<R> postForEntity(URI uri, B body, Class<R> clazz) {
        HttpEntity<B> request = httpRequestInjected(body);
        return restTemplate.postForEntity(uri, request, clazz);
    }

    private <B> HttpEntity<B> httpRequestInjected(B body) {
        Map<String, String> headersMap = new HashMap<>();
        tracer.inject(tracer.scopeManager().activeSpan().context(), Format.Builtin.HTTP_HEADERS, new TextMapAdapter(headersMap));
        HttpEntity<B> request = new HttpEntity<B>(body, httpHeaders(headersMap));
        return request;
    }

    private HttpHeaders httpHeaders(Map<String, String> map) {
        HttpHeaders headers = new HttpHeaders();
        map.forEach(headers::add);
        return headers;
    }
}
