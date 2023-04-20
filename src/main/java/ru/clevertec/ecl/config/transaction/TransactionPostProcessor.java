package ru.clevertec.ecl.config.transaction;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;

@RequiredArgsConstructor
@Component
public class TransactionPostProcessor implements BeanPostProcessor {

    private final SessionFactory sessionFactory;
    private final Map<String, Class<?>> beansWithTransactionClass = new HashMap<>();
    private final Map<String, Class<?>> beansWithTransactionMethod = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Transaction.class)) {
            beansWithTransactionClass.put(beanName, bean.getClass());
        }
        if (Arrays.stream(bean.getClass().getDeclaredMethods()).anyMatch(m -> m.isAnnotationPresent(Transaction.class))) {
            beansWithTransactionMethod.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beansWithTransactionClass.containsKey(beanName)) {
            return beanWithTransactionClass(bean);
        }
        if (beansWithTransactionMethod.containsKey(beanName)) {
            return beanWithTransactionMethod(bean);
        }
        return bean;
    }

    private Object beanWithTransactionClass(Object bean) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(bean);
        proxyFactory.addAdvice((MethodInterceptor) methodInvocation -> {
                    Session session = sessionFactory.getCurrentSession();
                    boolean isActiveTransaction = session.getTransaction().isActive();
                    if (!isActiveTransaction) {
                        session.beginTransaction();
                    }
                    try {
                        return methodInvocation.proceed();
                    } finally {
                        if (!isActiveTransaction) {
                            session.getTransaction().commit();
                        }
                    }
                }
        );
        return proxyFactory.getProxy();
    }

    private Object beanWithTransactionMethod(Object bean) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(bean);
        proxyFactory.addAdvice((MethodInterceptor) methodInvocation -> {
            if (methodInvocation.getMethod().isAnnotationPresent(Transaction.class)) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                try {
                    return methodInvocation.proceed();
                } finally {
                    session.getTransaction().commit();
                }
            } else {
                return methodInvocation.proceed();
            }
        });
        return proxyFactory.getProxy();
    }

}