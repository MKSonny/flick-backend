package pro.Flick.advertisement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdPageController {

    @GetMapping("/ad-1")
    public String adPage() {
        return "ad-page";
    }
}
