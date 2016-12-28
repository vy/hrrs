package com.vlkan.hrrs.api;

import javax.annotation.Nullable;
import java.io.Closeable;

public interface HttpRequestRecordReaderSource extends Closeable {

    @Nullable
    String read();

}
