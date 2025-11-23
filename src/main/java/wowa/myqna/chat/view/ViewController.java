package wowa.myqna.chat.view;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wowa.myqna.user.domain.UserEntity;
import wowa.myqna.user.repository.UserRepository;
import wowa.myqna.user.service.UserLowService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserLowService userLowService;
    @Value("${application.domain}")
    private String domain;

    @GetMapping("/link/{userId}")
    public String linkPage(@PathVariable String userId, Model model) {

        UserEntity findUser = userLowService.findById(userId);

        model.addAttribute("userId", findUser.getId());
        model.addAttribute("username", findUser.getUsername());
        model.addAttribute("domain", domain);
        return "link";
    }
}
