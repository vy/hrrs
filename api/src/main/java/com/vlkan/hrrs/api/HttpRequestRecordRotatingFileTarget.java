package com.vlkan.hrrs.api;

import com.vlkan.hrrs.api.util.CountingBufferedFileWriterListener;
import com.vlkan.hrrs.api.util.CountingBufferedFileWriter;
import com.vlkan.hrrs.api.util.FileRotationKey;
import com.vlkan.hrrs.api.util.FileRotationKeyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class HttpRequestRecordRotatingFileTarget implements HttpRequestRecordTarget, CountingBufferedFileWriterListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestRecordRotatingFileTarget.class);

    /**
     * Filename pattern used for generating files, must include {@literal %s} for the timestamp.
     */
    private final String filenamePattern;

    private final FileRotationKeyFactory rotationKeyFactory;

    /**
     * Allowed maximum number of bytes before rotation.
     */
    private final long maxByteCount;

    private volatile boolean rotating = false;

    private volatile FileRotationKey rotationKey;

    private volatile CountingBufferedFileWriter writer = null;

    public HttpRequestRecordRotatingFileTarget(String filenamePattern, FileRotationKeyFactory rotationKeyFactory, long maxByteCount) {
        checkFilenamePattern(filenamePattern);
        checkNotNull(rotationKeyFactory, "rotationKeyFactory");
        checkArgument(maxByteCount > 0, "maxByteCount > 0, found: %s", maxByteCount);
        this.filenamePattern = filenamePattern;
        this.rotationKeyFactory = rotationKeyFactory;
        this.maxByteCount = maxByteCount;
        this.rotationKey = rotationKeyFactory.getNextKey();
    }

    private static void checkFilenamePattern(String filenamePattern) {
        checkArgument(filenamePattern != null &&
                        filenamePattern.contains("%s") &&
                        filenamePattern.replaceFirst("%s", "").equals(
                                filenamePattern.replaceAll("%s", "")),
                "invalid filename pattern: %s", filenamePattern);
    }

    public String getFilenamePattern() {
        return filenamePattern;
    }

    public FileRotationKeyFactory getRotationKeyFactory() {
        return rotationKeyFactory;
    }

    public long getMaxByteCount() {
        return maxByteCount;
    }

    @Override
    public void write(String value) {
        try {
            writer.write(value);
        } catch (IOException error) {
            throw new RuntimeException("write failure", error);
        }
    }

    @Override
    public void onBufferedWrite(long byteCount) {
        if (shouldRotate(byteCount)) {
            rotate();
        }
    }

    private boolean shouldRotate(long byteCount) {
        return byteCount > maxByteCount || System.currentTimeMillis() >= rotationKey.getRotationTimeMillis();
    }

    private void rotate() {
        if (!rotating) {
            synchronized (this) {
                if (!rotating) {
                    rotating = true;
                    try {
                        long nextRotationTimeMillis = getNextRotationTimeMillis();
                        unsafeRotate(nextRotationTimeMillis);
                    } finally {
                        rotating = false;
                    }
                }
            }
        }
    }

    private void unsafeRotate(long nextRotationTimeMillis) {
        File file = createFile(nextRotationTimeMillis);
        CountingBufferedFileWriter newWriter = new CountingBufferedFileWriter(file, this);
        CountingBufferedFileWriter oldWriter = writer;
        writer = newWriter;
        if (oldWriter != null) {
            oldWriter.close();
        }
//        rotationTimeMillis = nextRotationTimeMillis;
        LOGGER.debug("completed rotation (oldWriter={}, newWriter={})", oldWriter, newWriter);
    }

    private File createFile(long nextRotationTimeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date nextRotationDate = new Date(nextRotationTimeMillis);
        String formattedNextRotationDate = dateFormat.format(nextRotationDate);
        String filename = filenamePattern.replace("%s", formattedNextRotationDate);
        return new File(filename);
    }

    private static long getMidnightTimeMillis() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long getNextRotationTimeMillis() {
        Calendar prevRotationCalendar = new GregorianCalendar();
//        prevRotationCalendar.setTimeInMillis(rotationTimeMillis);
        int prevRotationDayOfMonth = prevRotationCalendar.get(Calendar.DAY_OF_MONTH);
        Calendar nextRotationCalendar = new GregorianCalendar();
//        nextRotationCalendar.setTimeInMillis(rotationTimeMillis);
        int nextRotationDayOfMonth;
        do {
            nextRotationCalendar.add(Calendar.DAY_OF_MONTH, 1);
            nextRotationDayOfMonth = nextRotationCalendar.get(Calendar.DAY_OF_MONTH);
        } while (prevRotationDayOfMonth < nextRotationDayOfMonth);
        return nextRotationCalendar.getTimeInMillis();
    }

}
