package com.github.lizhiwei88.dependencies.agent.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lizhiwei88.dependencies.agent.entity.ApiDependencies;
import com.github.lizhiwei88.dependencies.agent.entity.DependenciesInfo;
import com.github.lizhiwei88.dependencies.agent.util.Utils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhiwei
 */
public class ApiReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiReportService.class);

    private static String reportUrl;

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final DependenciesInfo DEPENDENCIES_INFO = new DependenciesInfo();

    private static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(1,
            r -> new Thread(r, "report-thread"));

    public ApiReportService(String serviceName) {
        if (reportUrl == null) {
            throw new NullPointerException();
        }
        DEPENDENCIES_INFO.setService(serviceName);
        SCHEDULED_THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(() -> {
            if (DEPENDENCIES_INFO.getApiDependenciesList() != null) {
                report();
            }
        }, 2, 10, TimeUnit.SECONDS);
    }

    public static void setReportUrl(String reportUrl) {
        if (reportUrl == null) {
            throw new IllegalArgumentException("server url not set");
        }
        ApiReportService.reportUrl = Utils.httpHead(reportUrl) + "/api/dependencies";
    }

    public void updateApi(List<ApiDependencies> apiDependenciesList) {
        if (apiDependenciesList == null) {
            return;
        }
        DEPENDENCIES_INFO.setApiDependenciesList(apiDependenciesList);
    }

    private void report() {
        try {
            RequestBody requestBody = RequestBody.create(OBJECT_MAPPER.writeValueAsBytes(DEPENDENCIES_INFO),
                    MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(ApiReportService.reportUrl)
                    .post(requestBody)
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.isSuccessful()) {
                LOGGER.debug("report success");
            }
        } catch (Exception e) {
            LOGGER.error("report error", e);
        }
    }

}
