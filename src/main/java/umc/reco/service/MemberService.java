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
import umc.reco.dto.request.LoginDto;
import umc.reco.dto.response.TokenDto;
import umc.reco.entity.Member;
import umc.reco.jwt.JwtFilter;
import umc.reco.jwt.TokenProvider;
import umc.reco.repository.MemberRepository;

import java.util.Collections;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public ResponseEntity<TokenDto> join(LoginDto loginDto) {
        Member member = memberRepository.findByUuid(loginDto.getUuid());
        if (member == null)
            member = createNewMember(loginDto.getUuid(), loginDto.getEmail());

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), null,
                Collections.singleton(simpleGrantedAuthority));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication, member);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    private Member createNewMember(Long uuid, String email) {
        return memberRepository.save(new Member(uuid, email));
    }
}
