package pl.dryja.patternsearcher.persistance;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatternMatchingRepository extends MongoRepository<PatternMatchingTask, UUID> {
}
