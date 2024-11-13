package com.example.projectApp.Dto;

import com.example.projectApp.model.Profile;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePic;
    private UserDto user;

    public ProfileDto(Profile profile, boolean details) {
        this.id = profile.getId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.profilePic = profile.getProfilePic();

        // Only instantiate UserDto if details flag is true
        if (details) {
            this.user = profile.getUser() != null ? new UserDto(profile.getUser(), false) : null;
        }
    }

}