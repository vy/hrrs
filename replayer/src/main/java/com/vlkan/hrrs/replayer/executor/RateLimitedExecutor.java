package com.vlkan.hrrs.replayer.executor;

import com.google.common.util.concurrent.RateLimiter;
import com.vlkan.hrrs.replayer.cli.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class RateLimitedExecutor implements CloseableExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitedExecutor.class);

    private final ExecutorService executorService;

    private final RateLimiter rateLimiter;

    @Inject
    public RateLimitedExecutor(Config config) {

        // Check arguments.
        checkNotNull(config, "config");

        // Set class fields.
        this.executorService = createExecutorService(config.getThreadCount());
        this.rateLimiter = RateLimiter.create(
                config.getMaxRequestCountPerSecond(),
                config.getRampUpDurationSeconds(),
                TimeUnit.SECONDS);
        LOGGER.debug(
                "instantiated (threadCount={}, maxRequestCountPerSecond={}, rampUpDurationSeconds={})",
                config.getThreadCount(), config.getMaxRequestCountPerSecond(), config.getRampUpDurationSeconds());

    }

    private static ExecutorService createExecutorService(int threadCount) {
        final String name = RateLimitedExecutor.class.getSimpleName();
        final AtomicInteger threadCounter = new AtomicInteger();
        final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        return Executors.newFixedThreadPool(threadCount, runnable -> {
            checkNotNull(runnable, "runnable");
            String threadName = String.format("%s-%s", name, threadCounter.getAndIncrement());
            return new Thread(threadGroup, runnable, threadName);
        });
    }

    @Override
    public void execute(Runnable runnable) {
        checkNotNull(runnable, "runnable");
        rateLimiter.acquire();
        executorService.execute(runnable);
    }

    @Override
    public void close() {
        LOGGER.debug("closing");
        executorService.shutdown();
        try {
            boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);
            if (terminated) {
                LOGGER.debug("successfully terminated");
            } else {
                LOGGER.debug("termination await failure");
            }
        } catch (InterruptedException error) {
            LOGGER.error("termination await interrupted", error);
        }
    }

}
