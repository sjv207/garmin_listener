package uk.ac.exeter.feele.garminlistener.model;

import jakarta.persistence.*;
import java.sql.Date;
import lombok.Data;

@Entity
@Table(name = "user_health")
@Data
public class UserHealth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garmin_user_id", nullable = false)
    private GarminUser garminUser;

    @Column(name = "health_type", nullable = false, length = 45)
    private String healthType;

    @Column(name = "calendar_date", nullable = false)
    private Date calendarDate;

    @Lob
    @Column(name = "json_data", nullable = false)
    private byte[] jsonData;
}
