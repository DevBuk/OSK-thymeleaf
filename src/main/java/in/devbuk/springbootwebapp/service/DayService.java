package in.devbuk.springbootwebapp.service;

import in.devbuk.springbootwebapp.entity.Day;
import in.devbuk.springbootwebapp.entity.Employee;
import in.devbuk.springbootwebapp.entity.Hour;
import in.devbuk.springbootwebapp.entity.User;
import in.devbuk.springbootwebapp.repository.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DayService {


    @Autowired
    private DayRepository dayRepository;


    /**
     * Creates hours list for specific driving day for specified instructor (employee)
     *
     */
    public List<Hour> creatingHoursListForDrivingDay(){
        List<Hour> hoursList = new ArrayList<>();

        Hour eight_nine = new Hour("8-9");
        Hour nine_ten = new Hour("9-10");
        Hour ten_eleven = new Hour("10-11");
        Hour eleven_twelve = new Hour("11-12");
        Hour twelve_thirteen = new Hour("12-13");
        Hour thirteen_fourteen = new Hour("13-14");
        Hour fourteen_fifteen = new Hour("14-15");
        Hour fifteen_sixteen = new Hour("15-16");

        hoursList.add(eight_nine);
        hoursList.add(nine_ten);
        hoursList.add(ten_eleven);
        hoursList.add(eleven_twelve);
        hoursList.add(twelve_thirteen);
        hoursList.add(thirteen_fourteen);
        hoursList.add(fourteen_fifteen);
        hoursList.add(fifteen_sixteen);

        return hoursList;
    }

    /**
     *Checks the specific day for the specified instructor (employee) exist in database
     * if not it creates list of hours for that specified day and saves that day in database
     *
     */
    public Day savingDayToDBIfDoesntExistYetForTheSpecifiedEmployee(Day day){
        Employee specifiedEmployee = day.getEmployee();
        List<Day> listOfDay =  specifiedEmployee.getDaysOfEmployee();
        boolean sensor = false;
        for (Day oneDay : listOfDay ) {
            if (oneDay.getDrivingDay().toString().equals(day.getDrivingDay().toString())) {
                day = oneDay;
                sensor = true;
            }
        }
        if(sensor==false){
            DayService dayService = new DayService();
            day.setHour(dayService.creatingHoursListForDrivingDay());
            dayRepository.save(day);
        }
        return day;
    }

    /**
     * Gets username of current user.
     *
     */
    public String getUsernameOfCurrentUser (){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    /**
     *Assigns current user to hours selected by him specified day for specified instructor
     *
     */
    public Day assigningCurrentUserToHoursSelectedByHimSpecifiedDay(Day day, User currentUser){
        List<Hour>listOfHours = day.getHour();
        for (Hour oneHour : listOfHours){
            if(oneHour.getLocked() == true && oneHour.getUser() == null ){
                oneHour.setUser(currentUser);
            }
        }
        return day;
    }
}
