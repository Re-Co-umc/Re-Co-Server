package umc.reco.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LoginDto {
    private Long uuid;
    private String email;

    public LoginDto(Long uuid, String email) {
        this.uuid = uuid;
        this.email = email;
    }
}
