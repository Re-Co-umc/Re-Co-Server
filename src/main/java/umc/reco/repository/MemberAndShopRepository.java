package umc.reco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.reco.entity.MemberAndShop;

import java.util.List;
import java.util.Optional;

public interface MemberAndShopRepository extends JpaRepository<MemberAndShop, Long> {
    Optional<MemberAndShop> findByMemberIdAndShopId(Long memberId, Long shopId);

    List<MemberAndShop> findByMemberIdAndHeart(Long id, boolean heart);

}
