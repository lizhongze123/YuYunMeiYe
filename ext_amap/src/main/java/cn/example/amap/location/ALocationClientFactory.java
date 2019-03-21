package cn.example.amap.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class ALocationClientFactory {

    public static final String DEFAULT_TAG = "Default";

    public static AMapLocationClientOption createDefaultOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        option.setInterval(1000);
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        option.setHttpTimeOut(30000);
        //可选，设置是否返回逆地理地址信息。默认是true
        option.setNeedAddress(true);
        //设置是否返回缓存中位置，默认是true
        option.setLocationCacheEnable(false);
        return option;
    }

    public static AMapLocationClientOption createDefaultOption(long interval) {
        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        option.setInterval(interval);
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        option.setHttpTimeOut(30000);
        //可选，设置是否返回逆地理地址信息。默认是true
        option.setNeedAddress(true);
        //设置是否返回缓存中位置，默认是true
        option.setLocationCacheEnable(false);
        return option;
    }

    public static AMapLocationClientOption createSignOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        return option;
    }

    public static AMapLocationClientOption createOnceOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        option.setOnceLocation(true);
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        option.setHttpTimeOut(30000);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        option.setOnceLocationLatest(true);
        //可选，设置是否返回逆地理地址信息。默认是true
        option.setNeedAddress(true);
        //设置是否返回缓存中位置，默认是true
        option.setLocationCacheEnable(false);
        return option;
    }

    public static AMapLocationClient createLocationClient(Context context, AMapLocationClientOption option, AMapLocationListener listener) {
        AMapLocationClient client = new AMapLocationClient(context);
        client.setLocationOption(option);
        client.setLocationListener(listener);
        return client;
    }

    public static AMapLocationClient createDefaultLocationClient(Context context) {
        return createLocationClient(context, createDefaultOption(), new EventBusLocationListener(DEFAULT_TAG));
    }

    public static class EventBusLocationListener implements AMapLocationListener {

        private Object tag = null;

        public EventBusLocationListener(Object tag) {
            this.tag = tag;
        }

        public EventBusLocationListener() {
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(aMapLocation.getLongitude() == 0 && aMapLocation.getLatitude() == 0){
                return;
            }
//            EventBus.getDefault().post(new LocationMessage(aMapLocation, tag));
        }
    }

}
