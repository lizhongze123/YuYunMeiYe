package cn.example.amap.location;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;


public class ALocationManager {
    private static ALocationManager instance;

    private AMapLocationClient client;
    private AMapLocationClientOption option;

    private ALocationManager(Context context){
        client = new AMapLocationClient(context);
        client.setLocationListener(new ALocationClientFactory.EventBusLocationListener());
        option = ALocationClientFactory.createDefaultOption();
    }

    public static ALocationManager getInstance(Context context) {
        if(instance == null){
            synchronized (ALocationManager.class){
                instance = new ALocationManager(context);
            }
        }
        return instance;
    }

    public void setIntervel(long intervel){
        if(option != null){
            option.setInterval(intervel);
        }
    }

    public AMapLocationClientOption getOption() {
        return option;
    }

    public void setOption(AMapLocationClientOption option) {
        this.option = option;
    }

    public AMapLocationClient getClient() {
        return client;
    }

    public void startLocation(){
        if(!client.isStarted()) {
            client.startLocation();
        }
    }

    public void stopLocation(){
        if(client.isStarted()) {
            client.stopLocation();
        }
    }
}
