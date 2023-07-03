package umc.reco.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {
    private String member; // 이메일
    private String shop; // 가게 이름
    private String content;
    private Integer star;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponseDto(String member, String shop, String content,
                             Integer star, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.member = member;
        this.shop = shop;
        this.content = content;
        this.star = star;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
