package umc.reco.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import umc.reco.entity.Member;
import umc.reco.repository.MemberRepository;

@Component
public class UserUtil {
    private final MemberRepository memberRepository;

    public UserUtil(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getLoggedInMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return memberRepository.findByEmail(email);
    }
}
