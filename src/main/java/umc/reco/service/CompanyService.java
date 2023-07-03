package umc.reco.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.reco.entity.Company;
import umc.reco.exception.TargetNotFoundException;
import umc.reco.repository.CompanyRepository;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> findAllCompany() {
        List<Company> companyList = companyRepository.findAll();

        if(companyList.isEmpty()){
            throw new TargetNotFoundException("해당 참여기업 없습니다.");
        }
        return companyList;
    }

    public List<Company> findAllByCategory(int category) {
       List<Company> companyList = companyRepository.findAllByCategory(category);

       if(companyList.isEmpty()){
            throw new TargetNotFoundException("해당 참여기업 없습니다.");
       }
       return companyList;
    }

}
