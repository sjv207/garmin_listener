package uk.ac.exeter.feele.garminlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.exeter.feele.garminlistener.model.UserActivity;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    UserActivity findByActivityId(String activityId);    
}
