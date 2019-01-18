package com.lzx.lock.module.camera;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Size;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CompareSizeByArea implements java.util.Comparator<Size> {
    @Override
    public int compare(@NonNull Size lhs, @NonNull Size rhs) {

        // We cast here to ensure the multiplications won't overflow
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
    }
}
