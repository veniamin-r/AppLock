package com.lzx.lock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lzx.lock.base.AppConstants;
import com.lzx.lock.services.LockAccessibilityService;
import com.lzx.lock.services.LockService;
import com.lzx.lock.utils.SpUtil;

public class LockRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String type = intent.getStringExtra("type");
            if (type.contentEquals("lockservice"))
                context.startService(new Intent(context, LockService.class));
            else  if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_TYPE,false))
                context.startService(new Intent(context, LockAccessibilityService.class));
        }
    }
}
