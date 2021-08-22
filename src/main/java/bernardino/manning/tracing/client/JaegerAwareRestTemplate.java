package bernardino.manning.tracing.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JaegerAwareRestTemplate {
    private static final String [] HEADERS_TO_PROPAGATE = {
            "x-request-id",
            "x-b3-traceid",
            "x-b3-spanid",
            "x-b3-parentspanid",
            "x-b3-sampled",
            "x-b3-flags",
            "x-ot-span-context"};

    private final RestTemplate restTemplate;

    public JaegerAwareRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <B,R> ResponseEntity<R> postForEntity(URI uri, B body, Class<R> clazz) {
        HttpEntity<B> request = httpRequestInjected(body);
        return restTemplate.postForEntity(uri, request, clazz);
    }

    private <B> HttpEntity<B> httpRequestInjected(B body) {
        Map<String, String> headersMap = new HashMap<>();
        getCurrentRequest().ifPresent(request -> Arrays
                .stream(HEADERS_TO_PROPAGATE)
                .forEach(header -> Optional.ofNullable(request.getHeader(header))
                .ifPresent(value -> headersMap.put(header, value))));
        return new HttpEntity<>(body, httpHeaders(headersMap));
    }

    private HttpHeaders httpHeaders(Map<String, String> map) {
        HttpHeaders headers = new HttpHeaders();
        map.forEach(headers::add);
        return headers;
    }
    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(attrib -> ((ServletRequestAttributes) attrib).getRequest());
    }
}