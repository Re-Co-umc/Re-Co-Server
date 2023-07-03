package umc.reco.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.reco.dto.request.MlDto;
import umc.reco.dto.request.ShopDto;
import umc.reco.dto.response.MemberAndShopResponseDto;
import umc.reco.entity.Member;
import umc.reco.entity.MemberAndShop;
import umc.reco.entity.Shop;
import umc.reco.entity.Tree;
import umc.reco.exception.NotQualifiedDtoException;
import umc.reco.exception.TargetNotFoundException;
import umc.reco.repository.MemberAndShopRepository;
import umc.reco.repository.ShopRepository;
import umc.reco.repository.TreeRepository;
import umc.reco.util.UserUtil;

@Service
@Transactional
public class ShopService {

    private final ShopRepository shopRepository;
    private final MemberAndShopRepository memberAndShopRepository;
    private final UserUtil userUtil;
    private final TreeService treeService;

    private final TreeRepository treeRepository;


    public ShopService(ShopRepository shopRepository,
                       MemberAndShopRepository memberAndShopRepository, UserUtil userUtil,TreeService treeService,TreeRepository treeRepository) {
        this.shopRepository = shopRepository;
        this.memberAndShopRepository = memberAndShopRepository;
        this.userUtil = userUtil;
        this.treeService = treeService;
        this.treeRepository = treeRepository;
    }

    public Shop createShop(ShopDto shopDto) {
        if (shopDto.getName() == null || shopDto.getLatitude() == null || shopDto.getLongitude() == null)
            throw new NotQualifiedDtoException("DTO 값이 충족되지 않았습니다.");

        Shop targetShop = shopRepository.findByName(shopDto.getName());
        if (targetShop == null) {
            targetShop = shopRepository.save(
                    new Shop(shopDto.getName(), shopDto.getLatitude(), shopDto.getLongitude())
            );
        }

        return targetShop;
    }

    public MemberAndShopResponseDto like(Long id) {
        Shop findShop = shopRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 shop이 없습니다.")
        );
        Member member = userUtil.getLoggedInMember();

        MemberAndShop memberAndShop = memberAndShopRepository.findByMemberIdAndShopId(member.getId(), findShop.getId())
                .orElseGet(() -> createMemberAndShop(member, findShop));
        memberAndShop.setHeart(true);

        return new MemberAndShopResponseDto(member.getEmail(), findShop.getName(), memberAndShop.getHeart(),
                memberAndShop.getMl());
    }

    public MemberAndShopResponseDto likeCancel(Long id) {
        Shop findShop = shopRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 shop이 없습니다.")
        );
        Member member = userUtil.getLoggedInMember();
        MemberAndShop memberAndShop = memberAndShopRepository.findByMemberIdAndShopId(member.getId(), findShop.getId())
                .orElseThrow(() -> new IllegalStateException("좋아요 관계가 이루어지지 않았습니다."));
        memberAndShop.setHeart(false);

        return new MemberAndShopResponseDto(member.getEmail(), findShop.getName(), memberAndShop.getHeart(),
                memberAndShop.getMl());
    }

    private MemberAndShop createMemberAndShop(Member member, Shop shop) {
        return memberAndShopRepository.save(new MemberAndShop(member, shop));
    }

    public MemberAndShopResponseDto addMl(Long id,MlDto mlDto){

        Shop findShop = shopRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 shop이 없습니다.")
        );

        Member loggedInMember = userUtil.getLoggedInMember();
        Tree tree = treeService.getTreeByMember(loggedInMember);

        double totalMl = tree.getTotal_ml();
        double mlToAdd = mlDto.getMl();
        double updatedTotalMl = totalMl + mlToAdd;

        tree.setTotal_ml(updatedTotalMl);
        MemberAndShop memberAndShop = memberAndShopRepository.findByMemberIdAndShopId(loggedInMember.getId(), findShop.getId())
                .orElseGet(() -> createMemberAndShop(loggedInMember, findShop));
        memberAndShop.setMl(memberAndShop.getMl() + mlDto.getMl());
        return new MemberAndShopResponseDto(loggedInMember.getEmail(), findShop.getName(), memberAndShop.getHeart(),
                memberAndShop.getMl());
    }
}
