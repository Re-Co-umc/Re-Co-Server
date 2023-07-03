package umc.reco.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CompanyDto {

    private String companyName;
    private int companyCategory;
    private String companyLogo;

    public CompanyDto(String companyName, int companyCategory, String companyLogo) {
        this.companyName = companyName;
        this.companyCategory = companyCategory;
        this.companyLogo = companyLogo;
    }
}
