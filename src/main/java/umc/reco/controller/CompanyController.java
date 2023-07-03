package umc.reco.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.reco.dto.response.CompanyDto;
import umc.reco.entity.Company;
import umc.reco.service.CompanyService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/all")
    public ResponseEntity<List<CompanyDto>> allCompany() {
        List<Company> companyList = companyService.findAllCompany();
        List<CompanyDto> collect = companyList.stream()
                .map(m -> new CompanyDto(m.getCompanyName(), m.getCategory(), m.getCompanyLogo()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<CompanyDto>> findByCategory(@PathVariable("category") int category) {
        List<Company> companyList = companyService.findByCategory(category);
        List<CompanyDto> collect = companyList.stream()
                .map(m -> new CompanyDto(m.getCompanyName(), m.getCategory(), m.getCompanyLogo()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }
}
