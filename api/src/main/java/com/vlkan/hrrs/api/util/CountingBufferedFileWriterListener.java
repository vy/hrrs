package com.vlkan.hrrs.api.util;

public interface CountingBufferedFileWriterListener {

    void onBufferedWrite(long byteCount);

}
