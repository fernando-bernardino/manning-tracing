package bernardino.manning.tracing.api;


import bernardino.manning.tracing.client.BillingClient;
import bernardino.manning.tracing.client.DeliveryClient;
import bernardino.manning.tracing.client.InventoryClient;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class EShopController {

    private final JaegerTracer tracer;
    private final BillingClient billingClient;
    private final InventoryClient inventoryClient;
    private final DeliveryClient deliveryClient;

    public EShopController(
            JaegerTracer tracer,
            BillingClient billingClient,
            InventoryClient inventoryClient,
            DeliveryClient deliveryClient) {
        this.tracer = tracer;
        this.billingClient = billingClient;
        this.inventoryClient = inventoryClient;
        this.deliveryClient = deliveryClient;
    }

    @PostMapping(value = "/checkout")
    public ResponseEntity<String> checkout() {
        Span span = tracer.buildSpan("checkout").start();
        span.setBaggageItem("user", "tony");
        try (Scope scope = tracer.scopeManager().activate(span)){

            billingClient.doPayment();
            inventoryClient.createOrder();
            deliveryClient.arrangeDelivery();

            return ResponseEntity.ok("You have successfully checked out your shopping cart tony");
        } catch(Exception ex) {
            Tags.ERROR.set(span, true);
            span.log(Map.of(Fields.EVENT, "error", Fields.ERROR_OBJECT, ex, Fields.MESSAGE, ex.getMessage()));

            return ResponseEntity.internalServerError()
                    .body("Something went wrong, please contact our customer service.");
        } finally {
            span.finish();
        }
    }
}
