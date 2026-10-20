package com.jsu.aspect;

import com.jsu.anno.RequireRole;
import com.jsu.util.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 权限校验切面，配合 @RequireRole 注解使用
 */
@Slf4j
@Aspect
@Component
public class RoleCheckAspect {

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
        Integer currentRole = CurrentHolder.getCurrentRole();
        int[] excludedRoles = requireRole.exclude();

        if (currentRole != null && excludedRoles.length > 0) {
            for (int excluded : excludedRoles) {
                if (currentRole == excluded) {
                    log.warn("角色 {} 无权限执行: {}#{}",
                            currentRole,
                            joinPoint.getTarget().getClass().getSimpleName(),
                            joinPoint.getSignature().getName());
                    throw new SecurityException("无权限执行此操作");
                }
            }
        }
        return joinPoint.proceed();
    }
}
