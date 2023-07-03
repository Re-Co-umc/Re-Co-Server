package umc.reco.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ShopInfoDto {
    private String name;
    private Double latitude;
    private Double longitude;
    private Double star;
    private List<ReviewResponseDto> reviews;
}
