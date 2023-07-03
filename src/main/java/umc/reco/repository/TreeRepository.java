package umc.reco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.reco.entity.Member;
import umc.reco.entity.Tree;
import umc.reco.entity.TreeLevel;

public interface TreeRepository  extends JpaRepository<Tree,Long> {
    Tree findByMember(Member member);
}
