package uk.ac.exeter.feele.garminlistener.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_activity")
@Data
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garmin_user_id", nullable = false)
    private GarminUser garminUser;

    @Column(name = "activity_id", unique = true, nullable = false)
    private String activityId;

    @Column(name = "activity_type", nullable = false, length = 45)
    private String activityType;

    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @Column(name = "duration_in_seconds", nullable = false)
    private Integer durationInSeconds;

    @Lob
    @Column(name = "json_data", nullable = false)
    private String jsonData;
}
