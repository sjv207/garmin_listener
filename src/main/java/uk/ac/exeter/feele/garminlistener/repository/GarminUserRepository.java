package uk.ac.exeter.feele.garminlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.exeter.feele.garminlistener.model.GarminUser;

public interface GarminUserRepository extends JpaRepository<GarminUser, Long> {
}
