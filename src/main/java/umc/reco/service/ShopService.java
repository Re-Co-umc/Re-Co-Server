package umc.reco.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.reco.dto.request.MlDto;
import umc.reco.dto.request.ShopDto;
import umc.reco.dto.response.*;
import umc.reco.entity.*;
import umc.reco.exception.NotQualifiedDtoException;
import umc.reco.exception.TargetNotFoundException;
import umc.reco.repository.MemberAndShopRepository;
import umc.reco.repository.ReviewRepository;
import umc.reco.repository.ShopRepository;
import umc.reco.repository.TreeRepository;
import umc.reco.util.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShopService {

    private final ShopRepository shopRepository;
    private final MemberAndShopRepository memberAndShopRepository;
    private final UserUtil userUtil;
    private final TreeService treeService;

    private final TreeRepository treeRepository;
    private final ReviewRepository reviewRepository;


    public ShopService(ShopRepository shopRepository,
                       MemberAndShopRepository memberAndShopRepository, UserUtil userUtil,TreeService treeService,TreeRepository treeRepository,
                       ReviewRepository reviewRepository) {
        this.shopRepository = shopRepository;
        this.memberAndShopRepository = memberAndShopRepository;
        this.userUtil = userUtil;
        this.treeService = treeService;
        this.treeRepository = treeRepository;
        this.reviewRepository = reviewRepository;
    }

    public CommonDto createShop(ShopDto shopDto) {
        if (shopDto.getName() == null || shopDto.getLatitude() == null || shopDto.getLongitude() == null)
            throw new NotQualifiedDtoException("DTO 값이 충족되지 않았습니다.");

        Shop targetShop = shopRepository.findByName(shopDto.getName());
        if (targetShop == null) {
            targetShop = shopRepository.save(
                    new Shop(shopDto.getName(), shopDto.getLatitude(), shopDto.getLongitude())
            );
        }

        Object[] result = new Object[1];
        result[0] = targetShop;
        return new CommonDto(result);
    }

    public CommonDto like(Long id) {
        Shop findShop = shopRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 shop이 없습니다.")
        );
        Member member = userUtil.getLoggedInMember();

        MemberAndShop memberAndShop = memberAndShopRepository.findByMemberIdAndShopId(member.getId(), findShop.getId())
                .orElseGet(() -> createMemberAndShop(member, findShop));
        memberAndShop.setHeart(true);

        Object[] result = new Object[1];
        result[0] = new MemberAndShopResponseDto(member.getEmail(), findShop.getName(), memberAndShop.getHeart(),
                memberAndShop.getMl());
        return new CommonDto(result);
    }

    public CommonDto likeCancel(Long id) {
        Shop findShop = shopRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 shop이 없습니다.")
        );
        Member member = userUtil.getLoggedInMember();
        MemberAndShop memberAndShop = memberAndShopRepository.findByMemberIdAndShopId(member.getId(), findShop.getId())
                .orElseThrow(() -> new IllegalStateException("좋아요 관계가 이루어지지 않았습니다."));
        memberAndShop.setHeart(false);

        Object[] result = new Object[1];
        result[0] = new MemberAndShopResponseDto(member.getEmail(), findShop.getName(), memberAndShop.getHeart(),
                memberAndShop.getMl());
        return new CommonDto(result);
    }

    private MemberAndShop createMemberAndShop(Member member, Shop shop) {
        return memberAndShopRepository.save(new MemberAndShop(member, shop));
    }

    public CommonDto addMl(Long id,MlDto mlDto){

        Shop findShop = shopRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 shop이 없습니다.")
        );

        Member loggedInMember = userUtil.getLoggedInMember();
        Tree tree = treeService.getTreeByMember(loggedInMember);

        double updatedTotalPoint = countPoint(tree);

        double totalMl = tree.getTotal_ml();
        double mlToAdd = mlDto.getMl();
        double updatedTotalMl = totalMl + mlToAdd;

        tree.setTotal_ml(updatedTotalMl);
        tree.setPoint(updatedTotalPoint);
        MemberAndShop memberAndShop = memberAndShopRepository.findByMemberIdAndShopId(loggedInMember.getId(), findShop.getId())
                .orElseGet(() -> createMemberAndShop(loggedInMember, findShop));
        memberAndShop.setMl(memberAndShop.getMl() + mlDto.getMl());

        Object[] result = new Object[1];
        result[0] = new MemberAndShopResponseDto(loggedInMember.getEmail(), findShop.getName(), memberAndShop.getHeart(),
                memberAndShop.getMl());
        return new CommonDto(result);
    }

    public CommonDto getShopInfo(Long id) {
        Shop findShop = shopRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 shop이 없습니다.")
        );
        List<Review> findAllReviews = reviewRepository.findAllByShopId(id);

        ShopInfoDto shopInfoDto = new ShopInfoDto();
        shopInfoDto.setName(findShop.getName());
        shopInfoDto.setLatitude(findShop.getLatitude());
        shopInfoDto.setLongitude(findShop.getLongitude());

        long allStarValue = calculateShopStar(findAllReviews);
        shopInfoDto.setStar((double) allStarValue / findAllReviews.size());

        shopInfoDto.setReviews(extractReviewInfo(findAllReviews));

        Object[] result = new Object[1];
        result[0] = shopInfoDto;
        return new CommonDto(result);
    }

    public List<ReviewResponseDto> extractReviewInfo(List<Review> findAllReviews) {
        return findAllReviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getMember().getEmail(),
                        review.getShop().getName(),
                        review.getContent(),
                        review.getStar(),
                        review.getCreated(),
                        review.getModified()
                ))
                .collect(Collectors.toList());
    }

    private static long calculateShopStar(List<Review> findAllReviews) {
        return findAllReviews.stream()
                .mapToLong(Review::getStar)
                .sum();
    }

    private double countPoint(Tree tree){
        double totalPoint = tree.getPoint();
        double updatedTotalMl = totalPoint +2000;

        return updatedTotalMl;
    }

    public CommonDto searchAllShop() {
        List<Shop> shopList = shopRepository.findAll();
        Object[] result = new Object[1];
        result[0] = shopList;
        return new CommonDto(result);
    }
}
