package bernardino.manning.tracing;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class EShopController {

    @PostMapping(value = "/checkout")
    public ResponseEntity<String> checkout() {
        return ResponseEntity.ok("You have successfully checked out your shopping cart.");
    }
}
