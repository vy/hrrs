package com.vlkan.hrrs.api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.google.common.base.Preconditions.checkNotNull;

public class DailyFileRotationKeyFactory implements FileRotationKeyFactory {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'");

    @Override
    public FileRotationKey getNextKey() {
        long rotationTimeMillis = findRotationTimeMillis();
        return createRotationKey(rotationTimeMillis);
    }

    private static long findRotationTimeMillis() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public FileRotationKey getNextKey(FileRotationKey prevKey) {
        checkNotNull(prevKey, "prevKey");
        long nextRotationTimeMillis = findNextRotationTimeMillis(prevKey.getRotationTimeMillis());
        return createRotationKey(nextRotationTimeMillis);
    }

    private static long findNextRotationTimeMillis(long prevRotationTimeMillis) {
        Calendar prevRotationCalendar = new GregorianCalendar();
        prevRotationCalendar.setTimeInMillis(prevRotationTimeMillis);
        int prevRotationDayOfMonth = prevRotationCalendar.get(Calendar.DAY_OF_MONTH);
        Calendar nextRotationCalendar = new GregorianCalendar();
        nextRotationCalendar.setTimeInMillis(prevRotationTimeMillis);
        int nextRotationDayOfMonth;
        do {
            nextRotationCalendar.add(Calendar.DAY_OF_MONTH, 1);
            nextRotationDayOfMonth = nextRotationCalendar.get(Calendar.DAY_OF_MONTH);
        } while (prevRotationDayOfMonth < nextRotationDayOfMonth);
        return nextRotationCalendar.getTimeInMillis();
    }

    private static FileRotationKey createRotationKey(long rotationTimeMillis) {
        String fileKey = createFileKey(rotationTimeMillis);
        return ImmutableFileRotationKey
                .builder()
                .rotationTimeMillis(rotationTimeMillis)
                .fileKey(fileKey)
                .build();
    }

    private static String createFileKey(long rotationTimeMillis) {
        Date nextRotationDate = new Date(rotationTimeMillis);
        return DATE_FORMAT.format(nextRotationDate);
    }

}
