package com.vlkan.hrrs.replayer.executor;

import java.io.Closeable;
import java.util.concurrent.Executor;

public interface CloseableExecutor extends Closeable, Executor {

    // Do nothing.

}
