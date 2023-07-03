package umc.reco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.reco.entity.Review;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByMemberIdAndShopId(Long memberId, Long shopId);

    Optional<Review> findById(Long id);
}
