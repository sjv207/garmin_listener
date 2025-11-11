package uk.ac.exeter.feele.garminlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.exeter.feele.garminlistener.model.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
}
