package umc.reco.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ReviewRequestDto {
    private String content;
    private Integer star;
}
