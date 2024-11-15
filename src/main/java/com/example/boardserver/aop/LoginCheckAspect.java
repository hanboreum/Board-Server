package com.example.boardserver.aop;

import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
@Log4j2
public class LoginCheckAspect {

    @Around("@annotation(com.example.boardserver.aop.LoginCheck)")
    public Object adminLoginCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 현재 HTTP 세션을 가져옵니다.
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        String id = null;

        // 실행된 메소드에서 LoginCheck 어노테이션을 리플렉션으로 가져오기
        Method method = ((org.aspectj.lang.reflect.MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        LoginCheck loginCheck = method.getAnnotation(LoginCheck.class);

        if (loginCheck != null) {
            // 사용자 유형에 따른 로그인 ID를 가져옵니다.
            String userType = loginCheck.type().toString();
            switch (userType) {
                case "ADMIN": {
                    id = SessionUtil.getLoginAdminId(session);
                    break;
                }
                case "USER": {
                    id = SessionUtil.getLoginMemberId(session);
                    break;
                }
            }
        }

        if (id == null) {
            log.debug(proceedingJoinPoint.toString() + " accountName :" + id);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인한 id값을 확인해주세요.");
        }

        // 메소드의 인자에 로그인한 id를 세팅 (여기서는 첫 번째 인자로 설정하는 예시)
        Object[] modifiedArgs = proceedingJoinPoint.getArgs();
        if (modifiedArgs != null && modifiedArgs.length > 0) {
            modifiedArgs[0] = id; // 첫 번째 인자에 id값을 세팅
        }

        // 변경된 인자를 사용하여 메소드 실행
        return proceedingJoinPoint.proceed(modifiedArgs);
    }
}