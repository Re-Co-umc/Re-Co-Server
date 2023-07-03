package umc.reco.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.reco.dto.request.ReviewRequestDto;
import umc.reco.dto.response.CommonDto;
import umc.reco.dto.response.ReviewResponseDto;
import umc.reco.entity.Member;
import umc.reco.entity.Review;
import umc.reco.entity.Shop;
import umc.reco.exception.NotQualifiedDtoException;
import umc.reco.exception.TargetNotFoundException;
import umc.reco.repository.ReviewRepository;
import umc.reco.repository.ShopRepository;
import umc.reco.util.UserUtil;

@Service
@Transactional
public class ReviewService {

    private final UserUtil userUtil;
    private final ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;

    public ReviewService(UserUtil userUtil, ReviewRepository reviewRepository, ShopRepository shopRepository) {
        this.userUtil = userUtil;
        this.reviewRepository = reviewRepository;
        this.shopRepository = shopRepository;
    }

    public CommonDto createReview(Long id, ReviewRequestDto requestDto) {
        if (requestDto.getContent() == null || requestDto.getStar() == null) {
            throw new NotQualifiedDtoException("DTO 값이 충족되지 않았습니다.");
        }

        Shop findShop = shopRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 shop이 없습니다.")
        );
        Member member = userUtil.getLoggedInMember();

        Review newReview = reviewRepository.save(
                new Review(requestDto.getContent(), requestDto.getStar(), member, findShop));

        Object result = new ReviewResponseDto(member.getEmail(), findShop.getName(), newReview.getContent(),
                newReview.getStar(), newReview.getCreated(), newReview.getModified());

        return new CommonDto(result);
    }

    public CommonDto editReview(Long id, ReviewRequestDto requestDto) {

        Member member = userUtil.getLoggedInMember();

        if (requestDto.getContent() == null || requestDto.getStar() == null) {
            throw new NotQualifiedDtoException("DTO 값이 충족되지 않았습니다.");
        }

        Review findReview = reviewRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 리뷰가 없습니다.")
        );

        if (!findReview.getMember().equals(member)) {
            throw new IllegalStateException("해당 리뷰 작성자가 아닙니다.");
        }

        findReview.setContent(requestDto.getContent());
        findReview.setStar(requestDto.getStar());

        Object result = new ReviewResponseDto(member.getEmail(), findReview.getShop().getName(), findReview.getContent(),
                findReview.getStar(), findReview.getCreated(), findReview.getModified());
        return new CommonDto(result);
    }

    public void deleteReview(Long id) {
        Member member = userUtil.getLoggedInMember();

        Review findReview = reviewRepository.findById(id).orElseThrow(
                () -> new TargetNotFoundException("해당 리뷰가 없습니다.")
        );

        if (!findReview.getMember().equals(member)) {
            throw new IllegalStateException("해당 리뷰 작성자가 아닙니다.");
        }

        reviewRepository.deleteById(findReview.getId());
    }
}
