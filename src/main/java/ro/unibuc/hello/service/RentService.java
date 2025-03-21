package ro.unibuc.hello.service;

import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RentService {  
    
    @Autowired
    private RentRepository rentRepository;

    public Rent getRent(int userID, int gameID, LocalDate startDate) {
        return rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate)
                .orElseThrow(() -> new RuntimeException("Rent not found"));
    }
}
