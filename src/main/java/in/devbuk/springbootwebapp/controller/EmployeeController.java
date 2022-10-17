package in.devbuk.springbootwebapp.controller;

import in.devbuk.springbootwebapp.entity.Day;
import in.devbuk.springbootwebapp.entity.Employee;
import in.devbuk.springbootwebapp.repository.DayRepository;
import in.devbuk.springbootwebapp.repository.EmployeeRepository;
import in.devbuk.springbootwebapp.repository.HourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private HourRepository hourRepository;

    @GetMapping("/admin/showEmployees")
    public ModelAndView showEmployees() {
        ModelAndView mav = new ModelAndView("admin/list-employees");
        List<Employee> list = employeeRepository.findAll();
        mav.addObject("employees", list);
        return mav;
    }

    @GetMapping("/user/showEmployeesForClient")
    public ModelAndView showEmployeesForClient() {
        ModelAndView mav = new ModelAndView("user/list-employees-for-client");
        List<Employee> list = employeeRepository.findAll();
        mav.addObject("employees", list);
        return mav;
    }

    @GetMapping("/admin/showEmployeesForClientAdmin")
    public ModelAndView showEmployeesForClientAdmin(@RequestParam Long userId) {
        ModelAndView mav = new ModelAndView("admin/list-employees-for-client-admin");
        List<Employee> list = employeeRepository.findAll();
        mav.addObject("userId", userId);
        mav.addObject("employees", list);
        return mav;
    }

    @GetMapping("/admin/addEmployeeForm")
    public ModelAndView addEmployeeForm() {
        ModelAndView mav = new ModelAndView("admin/add-employee-form");
        Employee newEmployee = new Employee();
        mav.addObject("employee", newEmployee);
        return mav;
    }

    @GetMapping("/admin/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long employeeId) {
        ModelAndView mav = new ModelAndView("admin/add-employee-form");
        Employee employee = employeeRepository.findById(employeeId).get();
        mav.addObject("employee", employee);
        return mav;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/admin/showEmployees";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam Long employeeId) {
        Employee employee = employeeRepository.getById(employeeId);
        List<Day> daysOfEmployee = employee.getDaysOfEmployee();
        dayRepository.deleteAll(daysOfEmployee);
        employeeRepository.deleteById(employeeId);
        return "redirect:/admin/showEmployees";
    }
}
