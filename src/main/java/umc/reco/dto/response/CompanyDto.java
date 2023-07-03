package umc.reco.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CompanyDto {

    private Long companyId;
    private String companyName;
    private int companyCategory;
    private String companyLogo;

    public CompanyDto(Long companyId, String companyName, int companyCategory, String companyLogo) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyCategory = companyCategory;
        this.companyLogo = companyLogo;
    }
}
