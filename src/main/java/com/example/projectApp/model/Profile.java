package com.example.projectApp.model;
import com.example.projectApp.Dto.ProfileDto;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"profileId\"", nullable = false)
    private Long id;

    @Column(name = "\"firstName\"", nullable = false)
    private String firstName;

    @Column(name = "\"lastName\"", nullable = false)
    private String lastName;

    @Column(name = "\"profilePic\"")
    private String profilePic;

    @OneToOne(mappedBy = "profile", orphanRemoval = true)
    private User user;

    public Profile(ProfileDto dto) {
        this.id = dto.getId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.profilePic = dto.getProfilePic();
        this.user = dto.getUser() != null ?
                new User(dto.getUser()) :
                null;
    }
}