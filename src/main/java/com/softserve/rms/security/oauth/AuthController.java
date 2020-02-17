package com.softserve.rms.security.oauth;

import com.softserve.rms.entities.User;
import com.softserve.rms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login/oauth2")
    public String loadLoginPage(){
        return "login";
    }

    @GetMapping("/home")
    public  String home(){
        return  "home";
    }

//    @PostMapping("/signup")
//    public String login(@ModelAttribute("signup") User user){
//        String token = userService.save(user);
//        return UriComponentsBuilder.fromUriString(homeUrl)
//                .queryParam("auth_token", token)
//                .build().toUriString();
//    }
}
