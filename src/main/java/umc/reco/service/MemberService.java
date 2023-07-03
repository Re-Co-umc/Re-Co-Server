package umc.reco.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.reco.dto.request.EditProfileDto;
import umc.reco.dto.request.LoginDto;
import umc.reco.dto.response.*;
import umc.reco.entity.Member;
import umc.reco.entity.MemberAndShop;
import umc.reco.entity.Shop;
import umc.reco.entity.Tree;
import umc.reco.exception.NotQualifiedDtoException;
import umc.reco.jwt.JwtFilter;
import umc.reco.jwt.TokenProvider;
import umc.reco.repository.MemberAndShopRepository;
import umc.reco.repository.MemberRepository;
import umc.reco.repository.TreeRepository;
import umc.reco.util.UserUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final TreeRepository treeRepository;
    private final TokenProvider tokenProvider;
    private final UserUtil userUtil;
    private final MemberAndShopRepository memberAndShopRepository;

    public MemberService(MemberRepository memberRepository, TreeRepository treeRepository,
                         TokenProvider tokenProvider, UserUtil userUtil,MemberAndShopRepository memberAndShopRepository) {
        this.memberRepository = memberRepository;
        this.treeRepository = treeRepository;
        this.tokenProvider = tokenProvider;
        this.userUtil = userUtil;
        this.memberAndShopRepository=memberAndShopRepository;
    }

    public ResponseEntity<TokenDto> join(LoginDto loginDto) {
        if (loginDto.getEmail() == null || loginDto.getUuid() == null)
            throw new NotQualifiedDtoException("DTO 값이 충족되지 않았습니다.");

        Member member = memberRepository.findByUuid(loginDto.getUuid());
        if (member == null) {
            member = createNewMember(loginDto.getUuid(), loginDto.getEmail());
            createNewTree(member);
        }

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), null,
                Collections.singleton(simpleGrantedAuthority));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication, member);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    public CommonDto getProfile() {
        Member loggedInMember = userUtil.getLoggedInMember();
        Object[] result = new Object[1];
        result[0] = new ProfileDto(loggedInMember.getEmail(), loggedInMember.getNickname());
        return new CommonDto(result);
    }

    public CommonDto editProfile(EditProfileDto editProfileDto) {
        if (editProfileDto.getNickname() == null)
            throw new NotQualifiedDtoException("DTO 값이 충족되지 않았습니다.");

        Member loggedInMember = userUtil.getLoggedInMember();
        loggedInMember.setNickname(editProfileDto.getNickname());

        Object[] result = new Object[1];
        result[0] = new ProfileDto(loggedInMember.getEmail(), loggedInMember.getNickname());
        return new CommonDto(result);
    }

    private Member createNewMember(Long uuid, String email) {
        return memberRepository.save(new Member(uuid, email));
    }

    private void createNewTree(Member member) {
        Tree newTree = Tree.builder()
                .member(member)
                .point(0)
                .total_ml(0)
                .build();
        treeRepository.save(newTree);
    }

    @Transactional(readOnly = true)

  public CommonDto searchAll(){

        Member loggedInMember = userUtil.getLoggedInMember();
        List<MyListDto> likedShops = new ArrayList<>();

        List<MemberAndShop> memberAndShops = memberAndShopRepository.findByMemberIdAndHeart(loggedInMember.getId(), true);
        for (MemberAndShop memberAndShop : memberAndShops) {
            Shop shop = memberAndShop.getShop();
            MyListDto shopInfoDto = new MyListDto(shop.getName(), shop.getLatitude(),shop.getLongitude());
            likedShops.add(shopInfoDto);
        }

        Object[] result = new Object[1];
        result[0] = likedShops;
        return new CommonDto(result);
    }


}
