package bernardino.manning.tracing.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class BillingController {

    @PostMapping
    public ResponseEntity<?> doPayment(@RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok("You have successfully payed your order.");
    }
}