package umc.reco.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfileDto {

    private String email;

    private String nickname;

    public ProfileDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
