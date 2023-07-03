package umc.reco.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CommonDto {
    private Boolean isSuccess;
    private Integer code;
    private String message;
    private Object result;

    public CommonDto(Object result) {
        this.isSuccess = true;
        this.code = 200;
        this.message = "OK";
        this.result = result;
    }
}
