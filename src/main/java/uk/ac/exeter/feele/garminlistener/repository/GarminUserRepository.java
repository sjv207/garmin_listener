package uk.ac.exeter.feele.garminlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.exeter.feele.garminlistener.model.GarminUser;

public interface GarminUserRepository extends JpaRepository<GarminUser, Long> {
	// Derived query method to find a user by their external Garmin user id (column garmin_user_id)
	GarminUser findByGarminUserId(String garminUserId);
}
