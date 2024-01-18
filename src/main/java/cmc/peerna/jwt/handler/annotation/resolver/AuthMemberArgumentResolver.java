//package cmc.peerna.jwt.handler.annotation.resolver;
//
//import cmc.peerna.apiResponse.code.ResponseStatus;
//import cmc.peerna.apiResponse.exception.handler.MemberException;
//import cmc.peerna.domain.Member;
//import cmc.peerna.jwt.handler.annotation.AuthMember;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.MethodParameter;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//@Component
//@RequiredArgsConstructor
//public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        AuthMember authMember = parameter.getParameterAnnotation(AuthMember.class);
//        if (authMember == null) return false;
//        if (parameter.getParameterType().equals(Member.class) == false) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
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
//        if(memberId.equals(0L))
//            return MemberConverter.toMemberTemp(memberId);
//        else {
//            Member member = MemberConverter.toMember(memberId);
//            return member;
//        }
//    }
//
//}
