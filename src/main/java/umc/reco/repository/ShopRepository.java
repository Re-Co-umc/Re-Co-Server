package umc.reco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.reco.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Shop findByName(String name);
}
