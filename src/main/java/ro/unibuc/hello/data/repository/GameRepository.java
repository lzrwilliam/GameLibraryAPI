package ro.unibuc.hello.data.repository;

import ro.unibuc.hello.data.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameRepository extends MongoRepository<Game, Integer> {
    List<Game> findByGenre(String genre);
}
