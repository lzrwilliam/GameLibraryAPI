package ro.unibuc.hello.data.repository;

import ro.unibuc.hello.data.model.Rent;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentRepository extends MongoRepository<Rent, String> {
    

    Optional<Rent> findByUserIDAndGameIDAndStartDate(String userID, String gameID, LocalDate startDate);

}
