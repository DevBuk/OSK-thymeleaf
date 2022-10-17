package in.devbuk.springbootwebapp.controller;

import in.devbuk.springbootwebapp.entity.*;
import in.devbuk.springbootwebapp.repository.DayRepository;
import in.devbuk.springbootwebapp.repository.EmployeeRepository;
import in.devbuk.springbootwebapp.repository.UserRepository;
import in.devbuk.springbootwebapp.service.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * The class handles endpoints related to the days and hours of driving lessons.
 *
 */
@Controller
public class DayController {

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private DayService dayService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Allows selecting day of driving lessons for the selected instructor by customer
     *
     */
    @GetMapping("/user/addDayForm")
    public ModelAndView addDayForm(@RequestParam Long employeeId){

        ModelAndView mav = new ModelAndView("user/add-day-form");
        Employee employee = employeeRepository.findById(employeeId).get();
        Day newDay = new Day();

        User currentUser = userRepository.findByUsername(dayService.getUsernameOfCurrentUser());
        mav.addObject("day", newDay);
        mav.addObject("employee", employee);
        mav.addObject("currentUser", currentUser);
        return mav;
    }

    /**
     * Allows selecting day of driving lessons for the selected customer and instructor by admin
     *
     */
    @GetMapping("/admin/addDayFormAdmin")
    public ModelAndView addDayFormAdmin(@RequestParam Long employeeId, @RequestParam Long userId){

        ModelAndView mav = new ModelAndView("admin/add-day-form-admin");
        Employee employee = employeeRepository.findById(employeeId).get();
        Day newDay = new Day();
        User currentUser = userRepository.getById(userId);
        mav.addObject("day", newDay);
        mav.addObject("employee", employee);
        mav.addObject("currentUser", currentUser);
        return mav;
    }

    /**
     * Allows saving chosen day of driving lessons for the selected instructor in the database by customer
     *
     */
    @Secured({"ROLE_USER"})
    @PostMapping("/saveDay")
    public String saveDay(@ModelAttribute Day day){
        day = dayService.recordingDayToDBIfDoesntExistYetForTheSpecifiedEmployee(day);
        return "redirect:/user/chooseHours?dayId="+day.getId();
    }

    /**
     * Allows saving chosen day of driving lessons for the selected instructor in the database by customer
     *
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/saveDayAdmin")
    public String saveDay(@ModelAttribute Day day,@RequestParam Long userId){
        day = dayService.recordingDayToDBIfDoesntExistYetForTheSpecifiedEmployee(day);
        return "redirect:/admin/chooseHoursAdmin?dayId="+day.getId()+"&userId="+userId;
    }

    /**
     * Allows selecting hours of driving lessons for the selected customer and  instructor on the selected day by admin
     *
     */
    @GetMapping("/admin/chooseHoursAdmin")
    public ModelAndView addHoursToDayAdminForm(@RequestParam Long dayId, @RequestParam Long userId) {
        Day loadedDay = dayRepository.findById(dayId).get();
        ModelAndView mav = new ModelAndView("admin/hours-to-choose-admin");
        User currentUser = userRepository.getById(userId);
        mav.addObject("currentUser", currentUser);
        mav.addObject("loadedDay", loadedDay);
        return mav;
    }

    /**
     * Allows selecting hours of driving lessons for the selected instructor on the selected day by customer
     *
     */
    @GetMapping("/user/chooseHours")
    public ModelAndView addHoursToDayForm(@RequestParam Long dayId) {
        Day loadedDay = dayRepository.findById(dayId).get();
        ModelAndView mav = new ModelAndView("user/hours-to-choose");
        User currentUser = userRepository.findByUsername(dayService.getUsernameOfCurrentUser());
        mav.addObject("currentUser", currentUser);
        mav.addObject("loadedDay", loadedDay);
        return mav;
    }

    /**
     * Allows selecting hours of driving lessons for the selected instructor on the selected day
     *
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/saveHoursToDay")
    public String saveHoursToDay(@ModelAttribute Day day){
        User currentUser = userRepository.findByUsername(dayService.getUsernameOfCurrentUser());
        dayRepository.save(dayService.assigningCurrentUserToHoursSelectedByHimSpecifiedDay(day,currentUser));
        return "redirect:/";
    }

    /**
     * Allows saving hours of driving lessons for the selected customer and  instructor on the selected day by admin
     *
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/saveHoursToDayAdmin")
    public String saveHoursToDay(@ModelAttribute Day day, @RequestParam Long userId){
        User currentUser = userRepository.getById(userId);
        dayRepository.save(dayService.assigningCurrentUserToHoursSelectedByHimSpecifiedDay(day,currentUser));
        return "redirect:/";
    }
}
