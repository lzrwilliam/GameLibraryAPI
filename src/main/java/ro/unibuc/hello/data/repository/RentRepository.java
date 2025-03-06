package ro.unibuc.hello.data.repository;

import ro.unibuc.hello.data.model.Rent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentRepository extends MongoRepository<Rent, String> {
    
}
