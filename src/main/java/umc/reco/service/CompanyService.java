package umc.reco.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.reco.entity.Company;
import umc.reco.repository.CompanyRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> findAllCompany() {
        return companyRepository.findAll();
    }

    public List<Company> findByCategory(int category) {
        return companyRepository.findByCategory(category);
    }

}
