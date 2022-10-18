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

/**
 * The class handles endpoints related to the customer-users
 *
 */
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

    /**
     *Displays the list of the all customer-users
     *
     */
    @GetMapping("/admin/showUsers")
    public ModelAndView showUsers() {
        ModelAndView mav = new ModelAndView("admin/list-users");
        List<User> list = userRepository.findAll();
        mav.addObject("users", list);
        return mav;
    }

    /**
     *Allows getting data about the new customer-user by displaying a form
     *
     */
    @GetMapping("/admin/addUserForm")
    public ModelAndView addUserForm() {
        ModelAndView mav = new ModelAndView("admin/add-user-form");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    /**
     *Enables the modification of previously entered data about the customer-user by displaying a completed form
     *
     */
    @GetMapping("/admin/showUpdateUserForm")
    public ModelAndView showUpdateForm(@RequestParam Long userId) {
        ModelAndView mav = new ModelAndView("admin/add-user-form");
        User user = userRepository.findById(userId).get();
        mav.addObject("user", user);
        return mav;
    }

    /**
     *Saves data about the customer-user provided in the form
     *
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user) {
        String toEncodingPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(toEncodingPassword);
        userRepository.save(user);
        return "redirect:/admin/showUsers";
    }

    /**
     *Deletes the user if the user is not assigned to any driving lesson
     *
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        if(userService.ifUserIsAssignedToAnyDrivingClass(hourRepository, userId)){
            return "user/user-deleting-error";
        }
        userRepository.deleteById(userId);
        return "redirect:/admin/showUsers";
    }

    /**
     *Displays the list of the all booked driving lessons for the whole application
     *
     */
    @GetMapping("/admin/adminPanel")
    public ModelAndView showAdminPanel() {
        ModelAndView mav = new ModelAndView("/admin/admin-panel");
        List<Hour> hourList = hourRepository.findAll();
        mav.addObject("hours", hourList);
        return mav;
    }

    /**
     *Displays the list of the all booked driving lessons specified customer-user
     *
     */
    @GetMapping("/user/userPanel")
    public ModelAndView myLessons() {
    ModelAndView mav = new ModelAndView("user/user-panel");
    User currentUser = userRepository.findByUsername(userService.getUsernameOfCurrentUser());
    mav.addObject("hours", userService.getAllDrivingHoursListForTheSpecifiedUser(currentUser));
    return mav;
    }

    /**
     *Allows the customer-user updating the driving lesson hour that was previously booked on the specific day with the specific instructor
     *
     */
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

    /**
     *Allows the customer-user to update in form the account details of his profile
     *
     */
    @GetMapping("/user/updateUserSettings")
    public ModelAndView updateUserSettings() {
        ModelAndView mav = new ModelAndView("user/settings");
        User currentUser = userRepository.findByUsername(userService.getUsernameOfCurrentUser());
        mav.addObject("currentUser", currentUser);
        return mav;
    }

    /**
     *Allows the customer-user to save data of his profile details account put in the form
     *
     */
    @Secured("ROLE_USER")
    @PostMapping("/saveUserSettings")
    public String saveUserSettings(@ModelAttribute User user) {
        String toEncodingPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(toEncodingPassword);
        userRepository.save(user);
        return "redirect:/index";
    }

}
