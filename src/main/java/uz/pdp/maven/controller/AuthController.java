package uz.pdp.maven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.maven.dto.SignUpDTO;
import uz.pdp.maven.model.AuthUser;
import uz.pdp.maven.model.Upload;
import uz.pdp.maven.model.UserStatus;
import uz.pdp.maven.repository.UserRepo;
import uz.pdp.maven.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepo userRepo;
    private final UserService userService;

    public AuthController(UserRepo userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/auth/login");
        modelAndView.addObject("errorMessage", error);
        return modelAndView;
    }

    @GetMapping("/signup")
    public String signup() {
        return "/auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpDTO signUpDTO) {
        userService.save(signUpDTO);
        return "redirect:/auth/login";
    }

    @GetMapping("/showUsers")
    public ModelAndView showUsers(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/showUsers");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(){
        return "auth/logout";
    }
}
