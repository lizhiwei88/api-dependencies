package com.github.lizhiwei88.dependencies.agent;

import com.github.lizhiwei88.dependencies.agent.entity.ApiDependencies;
import com.github.lizhiwei88.dependencies.agent.report.ApiReportService;
import com.github.lizhiwei88.dependencies.agent.util.Utils;
import feign.Contract;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author Zhiwei Li
 */
public class FeignClientInterceptor {

    private static final LinkedHashMap<String, List<ApiDependencies>> FEIGN_CLIENT_MAP = new LinkedHashMap<>();

    private static ApiReportService apiReportService;

    private FeignClientInterceptor() {
    }

    @RuntimeType
    public static Object intercept(@Argument(0) BeanDefinitionRegistry registry,
                                   @Argument(1) AnnotationMetadata annotationMetadata,
                                   @Argument(2) Map<String, Object> attributes,
                                   @SuperCall Callable<?> callable)
            throws Exception {
        Class<?> feignClientApiClass = Class.forName(annotationMetadata.getClassName());
        ConfigurableBeanFactory beanFactory = registry instanceof ConfigurableBeanFactory
                ? (ConfigurableBeanFactory) registry : null;
        if (apiReportService == null) {
            assert beanFactory != null;
            Environment environment = beanFactory.getBean(Environment.class);
            apiReportService = new ApiReportService(environment.getProperty("spring.application.name"));
        }

        String targetService = getName(beanFactory, attributes);
        String path = getPath(beanFactory, attributes);
        Contract contract = new SpringMvcContract();
        List<ApiDependencies> apiDependenciesList = contract.parseAndValidateMetadata(feignClientApiClass)
                .stream()
                .map(methodMetadata -> {
                    ApiDependencies apiDependencies = new ApiDependencies();
                    apiDependencies.setMethod(methodMetadata.template().method());
                    apiDependencies.setTargetService(targetService);
                    apiDependencies.setUri(path + methodMetadata.template().path());
                    return apiDependencies;
                })
                .collect(Collectors.toList());
        List<ApiDependencies> valueList = FEIGN_CLIENT_MAP.putIfAbsent(targetService, apiDependenciesList);
        if (valueList != null) {
            valueList.addAll(apiDependenciesList);
        }
        apiReportService.updateApi(FEIGN_CLIENT_MAP.values().stream().flatMap(List::stream).collect(Collectors.toList()));
        // 原有函数执行
        return callable.call();
    }

    static String getName(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String name = (String) attributes.get("serviceId");
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("name");
        }
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("value");
        }
        name = resolve(beanFactory, name);
        return getName(name);
    }

    public static String getName(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }
        String host;
        try {
            String url = Utils.httpHead(name);
            host = new URI(url).getHost();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Assert.state(host != null, "Service id not legal hostname (" + name + ")");
        return name;
    }

    static String getPath(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String path = resolve(beanFactory, (String) attributes.get("path"));
        return Utils.getPath(path);
    }

    static String resolve(ConfigurableBeanFactory beanFactory, String value) {
        if (StringUtils.hasText(value)) {
            BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();
            String resolved = beanFactory.resolveEmbeddedValue(value);
            if (resolver == null) {
                return resolved;
            }
            Object evaluateValue = resolver.evaluate(resolved, new BeanExpressionContext(beanFactory, null));
            if (evaluateValue != null) {
                return String.valueOf(evaluateValue);
            }
            return null;
        }
        return value;
    }
}
