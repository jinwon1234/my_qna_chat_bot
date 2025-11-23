package wowa.myqna.embedding.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmbeddingViewController {

    @GetMapping
    public String uploadPage() {
        return "home";
    }
}
