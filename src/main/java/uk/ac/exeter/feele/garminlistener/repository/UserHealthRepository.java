package uk.ac.exeter.feele.garminlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.exeter.feele.garminlistener.model.UserHealth;

public interface UserHealthRepository extends JpaRepository<UserHealth, Long> {
}
