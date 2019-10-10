package com.xxj.yxwxr.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by caokun on 2019/8/16 10:49.
 */

public class PermissionHelpUtils {

    /**
     * READ_PHONE_STATE、WRITE_EXTERNAL_STORAGE 两个权限是必须权限，没有这两个权限SDK无法正常获得广告
     * WRITE_CALENDAR、ACCESS_FINE_LOCATION 是两个可选权限；没有不影响SDK获取广告；但是如果应用申请到该权限，会显著提升应用的广告收益
     *
     * READ_PHONE_STATE 获取设备信息
     * ACCESS_FINE_LOCATION  位置信息
     * WRITE_EXTERNAL_STORAGE 照片、媒体、文件
     */
    public static List<String> getMustPermissions() {
        return Arrays.asList(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static List<String> getNotMustPermissions() {
        return Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 校验必须权限是否授权
     */
    @TargetApi(23)
    public static boolean checkMustPermissions(Context context) {
        List<String> lackedPermission = new ArrayList<>();
        /**
         * READ_PHONE_STATE、WRITE_EXTERNAL_STORAGE 两个权限是必须权限，没有这两个权限SDK无法正常获得广告。
         */
        for (String permissions : getMustPermissions()) {
            if (!(context.checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(permissions);
            }
        }
        if (0 == lackedPermission.size()) {
            return true;
        }
        return false;
    }

    /**
     * 校验非必须权限是否授权
     */
    @TargetApi(23)
    public static boolean checkNotMustPermissions(Context context) {
        List<String> lackedPermission = new ArrayList<>();
        /**
         * READ_PHONE_STATE、WRITE_EXTERNAL_STORAGE 两个权限是必须权限，没有这两个权限SDK无法正常获得广告。
         */
        for (String permissions : getNotMustPermissions()) {
            if (!(context.checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(permissions);
            }
        }
        if (0 == lackedPermission.size()) {
            return true;
        }
        return false;
    }
}
