package bernardino.manning.tracing.api;


import bernardino.manning.tracing.client.BillingClient;
import bernardino.manning.tracing.client.DeliveryClient;
import bernardino.manning.tracing.client.InventoryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class EShopController {
    private final BillingClient billingClient;
    private final InventoryClient inventoryClient;
    private final DeliveryClient deliveryClient;

    public EShopController(
            BillingClient billingClient,
            InventoryClient inventoryClient,
            DeliveryClient deliveryClient) {
        this.billingClient = billingClient;
        this.inventoryClient = inventoryClient;
        this.deliveryClient = deliveryClient;
    }

    @PostMapping(value = "/checkout")
    public ResponseEntity<String> checkout() {
        try {

            billingClient.doPayment();
            inventoryClient.createOrder();
            deliveryClient.arrangeDelivery();

            return ResponseEntity.ok("You have successfully checked out your shopping cart.");
        } catch(Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Something went wrong, please contact our customer service.");
        }
    }
}
