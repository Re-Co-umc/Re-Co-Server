package umc.reco.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.reco.entity.Member;
import umc.reco.entity.Tree;
import umc.reco.repository.TreeRepository;

@Service
@Transactional
public class TreeService {
    private final TreeRepository treeRepository;
    public TreeService(TreeRepository treeRepository){
        this.treeRepository = treeRepository;
    }
    public Tree getTreeByMember(Member member){
        return treeRepository.findByMember(member);
    }

}
