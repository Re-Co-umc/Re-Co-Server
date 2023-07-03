package umc.reco.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CompanyDto {

    private String companyName;
    private int companyCategoryIdx;
    private String companyLogo;

    public CompanyDto(String companyName, int companyCategoryIdx, String companyLogo) {
        this.companyName = companyName;
        this.companyCategoryIdx = companyCategoryIdx;
        this.companyLogo = companyLogo;
    }
}
