package umc.reco.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberAndShopResponseDto {

    private String member; // 이메일
    private String shop; // 이름
    private Boolean heart; // 좋아요 여부
    private Long ml; // 용량

    public MemberAndShopResponseDto(String member, String shop, Boolean heart, Long ml) {
        this.member = member;
        this.shop = shop;
        this.heart = heart;
        this.ml = ml;
    }
}
