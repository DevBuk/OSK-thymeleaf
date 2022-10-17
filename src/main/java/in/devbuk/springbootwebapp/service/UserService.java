package in.devbuk.springbootwebapp.service;

import in.devbuk.springbootwebapp.entity.Hour;
import in.devbuk.springbootwebapp.entity.User;
import in.devbuk.springbootwebapp.repository.HourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private HourRepository hourRepository;

    @Autowired
    private DayService dayService;

    public List<Hour> getAllDrivingHoursListForTheSpecifiedUser(User currentUser){
        List<Hour> hourList = hourRepository.findAll();
        List<Hour> userHours = new ArrayList<>();
        for(Hour hourUser : hourList) {
            if(hourUser.getLocked() != null && hourUser.getLocked() == true){
                if(hourUser.getUser().getId() == currentUser.getId()){
                    userHours.add(hourUser);
                }
            }
        }
        return userHours;
    }

    public boolean ifUserIsAssignedToAnyDrivingClass(HourRepository hourRepository, Long userId){
        List<Hour> hourList = hourRepository.findAll();
        for(Hour hour : hourList){
            if(hour.getUser()!=null && hour.getUser().getId()==userId){
                return true;
            }
        }
        return false;
    }

    public String getUsernameOfCurrentUser (){
        return dayService.getUsernameOfCurrentUser();
    }

}
