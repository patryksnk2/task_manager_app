package app.task_manager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.ZoneId;

@Slf4j
@Component
public class TimeZoneInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<ZoneId> TZ = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String header = request.getHeader("Time-Zone");

        if (header != null && !header.isBlank()) {
            String zone = header.trim();
            TZ.set(ZoneId.of(zone));
            log.info("Request [{} {}] - Time-Zone header set to '{}'", method, uri, zone);
        } else {
            TZ.set(ZoneId.of("UTC"));
            log.info("Request [{} {}] - No Time-Zone header provided, defaulting to 'UTC'", method, uri);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        ZoneId zone = TZ.get();

        log.debug("Request [{} {}] - Clearing Time-Zone '{}'", method, uri, zone);
        TZ.remove();
    }
}
