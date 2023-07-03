package umc.reco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.reco.entity.MemberAndShop;

public interface MemberAndShopRepository extends JpaRepository<MemberAndShop, Long> {
}
