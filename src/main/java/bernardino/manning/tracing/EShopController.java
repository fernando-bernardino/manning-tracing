package bernardino.manning.tracing;


import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
@RequestMapping
public class EShopController {

    private final Tracer tracer;

    @Autowired
    public EShopController(Tracer tracer) {
        this.tracer = tracer;
    }

    @PostMapping(value = "/checkout")
    public ResponseEntity<String> checkout() {
        return trace(() -> ResponseEntity.ok("You have successfully checked out your shopping cart."),
                "checkout");
    }

    private <M> M trace(Supplier<M> toTrace, String method) {
        Span span = tracer.buildSpan(method).start();
        try {
            return toTrace.get();
        } finally {
            span.finish();
        }
    }
}
