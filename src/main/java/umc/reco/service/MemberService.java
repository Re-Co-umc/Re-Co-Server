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
import umc.reco.dto.response.ProfileDto;
import umc.reco.dto.response.TokenDto;
import umc.reco.entity.Member;
import umc.reco.entity.Tree;
import umc.reco.exception.NotQualifiedDtoException;
import umc.reco.jwt.JwtFilter;
import umc.reco.jwt.TokenProvider;
import umc.reco.repository.MemberRepository;
import umc.reco.repository.TreeRepository;
import umc.reco.util.UserUtil;

import java.util.Collections;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final TreeRepository treeRepository;
    private final TokenProvider tokenProvider;
    private final UserUtil userUtil;

    public MemberService(MemberRepository memberRepository, TreeRepository treeRepository,
                         TokenProvider tokenProvider, UserUtil userUtil) {
        this.memberRepository = memberRepository;
        this.treeRepository = treeRepository;
        this.tokenProvider = tokenProvider;
        this.userUtil = userUtil;
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

    public ProfileDto getProfile() {
        Member loggedInMember = userUtil.getLoggedInMember();
        return new ProfileDto(loggedInMember.getEmail(), loggedInMember.getNickname());
    }

    public ProfileDto editProfile(EditProfileDto editProfileDto) {
        if (editProfileDto.getNickname() == null)
            throw new NotQualifiedDtoException("DTO 값이 충족되지 않았습니다.");

        Member loggedInMember = userUtil.getLoggedInMember();
        loggedInMember.setNickname(editProfileDto.getNickname());

        return new ProfileDto(loggedInMember.getEmail(), loggedInMember.getNickname());
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
}
