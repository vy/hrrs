package com.vlkan.hrrs.api;

import javax.annotation.Nullable;
import java.io.Closeable;

public interface HttpRequestRecordReaderSource<T> extends Closeable {

    @Nullable
    T read();

}
