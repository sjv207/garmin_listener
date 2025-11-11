package uk.ac.exeter.feele.garminlistener.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Entity
@Table(name = "user_token")
@Data
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private GarminUser user;

    @Column(name = "access_token", length = 1024)
    private String accessToken;

    @Column(name = "access_token_expires")
    private Integer accessTokenExpires;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name = "refresh_token", length = 200)
    private String refreshToken;

    @Column(name = "refresh_token_expires")
    private Integer refreshTokenExpires;

    @Column(nullable = false, length = 250)
    private String scope;

    @Column(length = 100)
    private String jti;

    @Column(name = "created", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created;
}
