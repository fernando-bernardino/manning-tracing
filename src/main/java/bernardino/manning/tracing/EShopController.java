package bernardino.manning.tracing;


import bernardino.manning.tracing.billing.BillingService;
import bernardino.manning.tracing.inventory.InventoryService;
import bernardino.manning.tracing.logistics.DeliveryService;
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

    private final InventoryService inventoryService;

    private final BillingService billingService;

    private final DeliveryService deliveryService;

    public EShopController(JaegerTracer tracer,
                           InventoryService inventoryService,
                           BillingService billingService,
                           DeliveryService deliveryService) {
        this.tracer = tracer;
        this.inventoryService = inventoryService;
        this.billingService = billingService;
        this.deliveryService = deliveryService;
    }

    @PostMapping(value = "/checkout")
    public ResponseEntity<String> checkout() {
        Span span = tracer.buildSpan("checkout").start();
        try (Scope scope = tracer.scopeManager().activate(span)){
            inventoryService.createOrder();
            billingService.payment();
            deliveryService.arrangeDelivery();

            return ResponseEntity.ok("You have successfully checked out your shopping cart.");
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
