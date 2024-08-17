package uz.pdp.maven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.maven.config.security.SessionUser;
import uz.pdp.maven.dto.TodoDto;
import uz.pdp.maven.model.AuthUser;
import uz.pdp.maven.model.Todo;
import uz.pdp.maven.model.Upload;
import uz.pdp.maven.repository.TodoRepo;
import uz.pdp.maven.repository.UploadRepo;
import uz.pdp.maven.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/main")
public class HomeController {

    private final TodoRepo todoRepo;
    private final SessionUser sessionUser;
    private final UploadRepo uploadRepo;

    public HomeController(TodoRepo todoRepo, SessionUser sessionUser, UploadRepo uploadRepo) {
        this.todoRepo = todoRepo;
        this.sessionUser = sessionUser;
        this.uploadRepo = uploadRepo;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();

        AuthUser user = sessionUser.getUser();
        List<Todo> todoList = todoRepo.getAllByOwnerId(user.getId());
        Upload upload = uploadRepo.getByUserId(user.getId());

        modelAndView.addObject("user", user);
        modelAndView.addObject("upload", upload);
        modelAndView.addObject("todos", todoList);
        modelAndView.setViewName("/main/home");
        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView profile() {
        ModelAndView modelAndView = new ModelAndView();
        AuthUser user = sessionUser.getUser();
        Upload upload = uploadRepo.getByUserId(user.getId());

        modelAndView.addObject("user", user);
        modelAndView.addObject("upload", upload);
        modelAndView.setViewName("/main/profile");
        return modelAndView;
    }

    @GetMapping("/createTodo")
    public ModelAndView createTodoForm() {
        AuthUser user = sessionUser.getUser();
        ModelAndView modelAndView = new ModelAndView();

        Upload upload = uploadRepo.getByUserId(user.getId());
        modelAndView.addObject("upload", upload);

        modelAndView.setViewName("/main/create-todo");
        return modelAndView;
    }

    @PostMapping("/createTodo")
    public String createTodo(@ModelAttribute TodoDto todoDto) {
        AuthUser user = sessionUser.getUser();

        Todo todo = Todo.builder()
                .deadline(todoDto.deadline())
                .name(todoDto.name())
                .description(todoDto.description())
                .ownerId(user.getId()).build();
        todoRepo.save(todo);

        return "redirect:/main/home";
    }

    @GetMapping("todo/update/{id}")
    public ModelAndView updateTodoForm(@PathVariable int id) {
        AuthUser user = sessionUser.getUser();
        ModelAndView modelAndView = new ModelAndView();

        Upload upload = uploadRepo.getByUserId(user.getId());
        Todo todo = todoRepo.get(id);

        modelAndView.addObject("upload", upload);
        modelAndView.addObject("todo", todo);
        modelAndView.setViewName("/main/update-todo");

        return modelAndView;
    }

    @PostMapping("todo/update")
    public String updateTodo(@ModelAttribute Todo todo) {
        AuthUser user = sessionUser.getUser();
        todo.setOwnerId(user.getId());

        todoRepo.update(todo);
        return "/main/home";
    }

    @PostMapping("todo/delete/{id}")
    public String deleteTodo(@PathVariable int id) {
        todoRepo.delete(id);
        return "redirect:/main/home";
    }
}
