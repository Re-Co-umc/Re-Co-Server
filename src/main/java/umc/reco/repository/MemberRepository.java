package umc.reco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.reco.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    Member findByUuid(Long uuid);
}
