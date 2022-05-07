package com.demo.example.aop.aop;

/**
 * Created with IntelliJ IDEA.
 * 切面
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class Aspect {

    /**
     * 切点表达式(正在表达式)
     */
    private Pointcut pointcut;

    /**
     * 增强通知
     */
    private Advice advice;

    public Aspect(Pointcut pointcut, Advice advice) {
        super();
        this.pointcut = pointcut;
        this.advice = advice;
    }

    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
}
