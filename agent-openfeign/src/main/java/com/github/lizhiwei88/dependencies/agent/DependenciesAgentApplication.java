package com.github.lizhiwei88.dependencies.agent;

import com.github.lizhiwei88.dependencies.agent.report.ApiReportService;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * @author Zhiwei Li
 */
public class DependenciesAgentApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(DependenciesAgentApplication.class);

    private DependenciesAgentApplication() {
    }

    public static void premain(String args, Instrumentation instrumentation) {
        // 设置上报服务
        ApiReportService.setReportUrl(args);
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("org.springframework.cloud.openfeign.FeignClientsRegistrar"))
                .transform(new Transformer("registerFeignClient", FeignClientInterceptor.class))
                .with(new Listener())
                .installOn(instrumentation);
    }

    private static class Transformer implements AgentBuilder.Transformer {

        private final String methodName;

        private final Class<?> interceptorClass;

        public Transformer(String methodName, Class<?> interceptorClass) {
            this.methodName = methodName;
            this.interceptorClass = interceptorClass;
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {
            return builder
                    .method(ElementMatchers.named(methodName))
                    .intercept(MethodDelegation.to(interceptorClass));
        }
    }

    private static class Listener implements AgentBuilder.Listener {

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, DynamicType dynamicType) {
            LOGGER.debug("On Transformation class {}.", typeDescription.getName());
        }

        @Override
        public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
            //do something
        }

        @Override
        public void onError(String typeName, ClassLoader classLoader, JavaModule module, Throwable throwable) {
            LOGGER.error("Enhance class {} error.", typeName, throwable);
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module) {
            //do something
        }
    }
}
