package uk.ac.exeter.feele.garminlistener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.exeter.feele.garminlistener.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
