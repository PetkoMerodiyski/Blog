package blog.controller;

import blog.bindingModel.UserBindingModel;
import blog.entity.Role;
import blog.entity.User;
import blog.repository.RoleRepository;
import blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static java.util.Base64.*;

@Controller
public class UserController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("view","user/register");
        return "index";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel) throws IOException {

        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
            return "redirect:/register";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

        User user=new User(userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword()));

        byte[] arr=userBindingModel.getFile().getBytes();
        String encoded;
        if(arr.length != 0){
            encoded= getEncoder().encodeToString(arr);
            user.setPicture(encoded);
        }


        Role userRole=this.roleRepository.findByName("ROLE_USER");
        user.addRole(userRole);

        this.userRepository.saveAndFlush(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view","user/login");

        return "index";
    }

    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        if(auth!=null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        return "redirect:/login?logout";
    }


    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){
        UserDetails principal =(UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        model.addAttribute("user",user);
        model.addAttribute("view","user/profile");

        return "index";
    }

}
