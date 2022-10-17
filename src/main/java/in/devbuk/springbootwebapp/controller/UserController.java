package in.devbuk.springbootwebapp.controller;

import in.devbuk.springbootwebapp.entity.*;
import in.devbuk.springbootwebapp.repository.*;
import in.devbuk.springbootwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HourRepository hourRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/admin/showUsers")
    public ModelAndView showUsers() {
        ModelAndView mav = new ModelAndView("admin/list-users");
        List<User> list = userRepository.findAll();
        mav.addObject("users", list);
        return mav;
    }

    @GetMapping("/admin/addUserForm")
    public ModelAndView addUserForm() {
        ModelAndView mav = new ModelAndView("admin/add-user-form");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    @GetMapping("/admin/showUpdateUserForm")
    public ModelAndView showUpdateForm(@RequestParam Long userId) {
        ModelAndView mav = new ModelAndView("admin/add-user-form");
        User user = userRepository.findById(userId).get();
        mav.addObject("user", user);
        return mav;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user) {
        String toEncodingPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(toEncodingPassword);
        userRepository.save(user);
        return "redirect:/admin/showUsers";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        if(userService.ifUserIsAssignedToAnyDrivingClass(hourRepository, userId)){
            return "user/user-deleting-error";
        }
        userRepository.deleteById(userId);
        return "redirect:/admin/showUsers";
    }

    @GetMapping("/admin/adminPanel")
    public ModelAndView showEmployees() {
        ModelAndView mav = new ModelAndView("/admin/admin-panel");
        List<Hour> hourList = hourRepository.findAll();
        mav.addObject("hours", hourList);
        return mav;
    }

    @GetMapping("/user/userPanel")
    public ModelAndView myLessons() {
    ModelAndView mav = new ModelAndView("user/user-panel");
    User currentUser = userRepository.findByUsername(userService.getUsernameOfCurrentUser());
    mav.addObject("hours", userService.getAllDrivingHoursListForTheSpecifiedUser(currentUser));
    return mav;
    }

    @GetMapping("/showUpdatePanelRow")
    public ModelAndView showUpdatePanelRow(@RequestParam Long hourId) {
        ModelAndView mav = new ModelAndView("user/panel-edition");
        Hour hour = hourRepository.findById(hourId).get();
        User user = hour.getUser();
        Day day = hour.getDay();
        mav.addObject("currentUser", user);
        mav.addObject("loadedDay", day);
        return mav;
    }

    @GetMapping("/user/updateUserSettings")
    public ModelAndView updateUserSettings() {
        ModelAndView mav = new ModelAndView("user/settings");
        User currentUser = userRepository.findByUsername(userService.getUsernameOfCurrentUser());
        mav.addObject("currentUser", currentUser);
        return mav;
    }

    @Secured("ROLE_USER")
    @PostMapping("/saveUserSettings")
    public String saveUserSettings(@ModelAttribute User user) {
        String toEncodingPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(toEncodingPassword);
        userRepository.save(user);
        return "redirect:/index";
    }

}
