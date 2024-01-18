package cmc.peerna.jwt.handler.annotation.resolver;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.jwt.JwtProvider;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        AuthMember authMember = parameter.getParameterAnnotation(AuthMember.class);
        if (authMember == null) return false;
        if (parameter.getParameterType().equals(Member.class) == false) {
            return false;
        }
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        Object principal = null;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null) {
//            principal = authentication.getPrincipal();
//        }
//        if (principal == null || principal.getClass() == String.class) {
//            throw new MemberException(ResponseStatus.MEMBER_NOT_FOUND);
//        }
//
//        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
//        Long memberId = Long.valueOf(authentication.getName());
//
//        Member member = MemberConverter.toMemberById(memberId);
//        return member;
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String jwt = jwtProvider.resolveToken(request);
        if (StringUtils.hasText(jwt)
                && jwtProvider.validateToken(jwt)) {
            // 토큰에서 사용자 ID (subject) 추출
            String userId = jwtProvider.getAuthentication(jwt).getName();
            return memberService.findById(Long.valueOf(userId));
        }
        return null; // 토큰이 없거나 유효하지 않은 경우
    }
}