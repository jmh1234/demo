package com.demo.aspect;

import com.demo.annotation.AdviceAspect;
import com.demo.constant.ProgramConstant;
import com.demo.entity.RespJson;
import com.demo.util.LoggerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * 通知切面
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
@Aspect
@Component
public class Advice {

    private static final Logger logger = LoggerUtil.getInstance(Advice.class);

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    private Advice() {
    }

    /**
     * 切面的调用条件
     */
    @Pointcut("execution(public * com.demo.controller.*.*(..)) && @annotation(com.demo.annotation.AdviceAspect)")
    public void addAdvice() {
    }

    @Around("addAdvice()")
    public Object advice(ProceedingJoinPoint process) {
        // 被注解“AdviceAspect”标记的方法的参数
        Object[] args = process.getArgs();
        // 被注解“AdviceAspect”标记的方法名
        String methodName = process.getSignature().getName();

        // 判断传入的参数是否符合要求(权限校验)
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) arg;
                if (ProgramConstant.TWO.equals(request.getParameter(ProgramConstant.ID))) {
                    return new RespJson(false, "对不起了兄弟，你么得权限了!", ProgramConstant.ERROR, null);
                }
            }
        }

        // 被注解“AdviceAspect”标记的方法的执行结果
        Object result = null;
        try {
            // 获取注解方法的反射
            Signature sig = process.getSignature();
            if (!(sig instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法!");
            }
            MethodSignature sigMethod = (MethodSignature) sig;
            Object target = process.getTarget();
            Method method = target.getClass().getMethod(sigMethod.getName(), sigMethod.getParameterTypes());

            // 获取 注解中的参数 只适用于不重复注解
            AdviceAspect annotation = method.getDeclaredAnnotationsByType(AdviceAspect.class)[0];
            if (annotation != null) {
                logger.info("注解传入的参数为: {}", annotation.description());
            }

            // 注入通知 标志方法执行状态
            logger.info("方法 {} 开始执行!", methodName);
            long startTime = System.currentTimeMillis();
            Object cache = redisTemplate.opsForValue().get(sigMethod.getName());
            if (cache != null) {
                logger.info("Get value from Cache");
                result = cache;
            } else {
                result = process.proceed();
                redisTemplate.opsForValue().set(sigMethod.getName(), result);
            }
            long endTime = System.currentTimeMillis();
            logger.info("方法 {} 执行结束!方法运行时间: {}ms", methodName, endTime - startTime);
        } catch (Throwable e) {
            logger.error(LoggerUtil.handleException(e));
        }
        return result;
    }
}
