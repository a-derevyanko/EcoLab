package org.ekolab.server.dev;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ekolab.server.common.Profiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Аспект, подсчитывающий время выполнения вызовов всех сервисов.
 * Активируется в режиме разработки.
 */
@Aspect
@Component
@Profile(Profiles.MODE.DEV)
public class LogExecutionTimeAspect {
    private static final String LOG_MESSAGE_FORMAT = "%s.%s execution time: %dms";
    private static final Logger LOG = LoggerFactory.getLogger(LogExecutionTimeAspect.class);

    @Pointcut("execution(@org.ekolab.server.dev.LogExecutionTime * *.*(..))")
    private void annotatedMethod() {}

    @Pointcut("execution(* org.ekolab..*.*(..))")
    private void anyMethod() {}

    @Around("annotatedMethod() && @annotation(annotation)")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint, LogExecutionTime annotation) throws Throwable {
        return logExecutionTime(joinPoint, annotation.normalExecutionTime());
    }

    @Around("!annotatedMethod() && anyMethod()")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, 100L);
    }

    private Object logExecutionTime(ProceedingJoinPoint joinPoint, long normalExecutionTime) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            if (stopWatch.getTotalTimeMillis() > normalExecutionTime) {
                String logMessage = String.format(LOG_MESSAGE_FORMAT,
                        joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis());
                LOG.warn(logMessage);
            }
        }
    }
}