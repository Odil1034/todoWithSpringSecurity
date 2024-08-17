package uz.pdp.maven.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.maven.model.AuthUser;
import uz.pdp.maven.repository.UserRepo;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepo userRepo;

    public AdminController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/showUsers")
    public String showUsers(Model model) {
        List<AuthUser> users = userRepo.getAll();
        users.forEach(System.out::println);
        model.addAttribute("users", users);
        return "/admin/showUsers";
    }

    @GetMapping("/block/{id}")
    public String blockUser(@PathVariable int id) {
        userRepo.blockUser(id);
        return "redirect:/admin/showUsers";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userRepo.deleteUser(id);
        return "redirect:/admin/showUsers";
    }
}
