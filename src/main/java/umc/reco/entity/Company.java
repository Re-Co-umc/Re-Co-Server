package umc.reco.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Company {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    @JsonIgnore
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_category")
    private int categoryIdx;

    @Column(name = "company_logo")
    private String companyLogo;
}
