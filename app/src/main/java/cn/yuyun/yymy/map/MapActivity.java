package cn.yuyun.yymy.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.autonavi.ae.gmap.maploader.ERROR_CODE;

import java.util.ArrayList;
import java.util.List;

import cn.example.amap.location.ALocationClientFactory;
import cn.example.amap.location.GeoCoderUtil;
import cn.example.amap.model.LatLngEntity;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.ui.home.attendance.SearchLocationActivity;
import cn.yuyun.yymy.ui.home.attendance.SearchLocationActivity2;
import cn.yuyun.yymy.ui.home.attendance.SearchResultAdapter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;


public class MapActivity extends BaseNoTitleActivity implements View.OnClickListener, AMapLocationListener, AdapterView.OnItemClickListener, AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener {

    private MapView mMapView = null;
    private AMap aMap;

    private ListView lv_data;
    private LinearLayout ll_poi;
    private String resultAddress;
    private AMapLocationClient locationClient;
    private LocationBean currentLoc;

    private LatLng checkinpoint, mlocation;
    private LatLonPoint searchLatlonPoint;
    private List<PoiItem> resultData;
    private SearchResultAdapter searchResultAdapter;
    private PoiSearch poisearch;
    private Circle mcircle;
    private boolean isItemClickAction, isLocationAction, isMove;
    private String firstDesc;
    private long mSearchId = 0;

    private Bundle savedInstanceState;

    private int REQUEST_CODE = 1111;

    public static Intent actionView(Context context, String value) {
        return new Intent(context, MapActivity.class).putExtra(EXTRA_DATA, value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews(savedInstanceState);
        locationClient = ALocationClientFactory.createLocationClient(this, ALocationClientFactory.createOnceOption(), this);
        locationClient.startLocation();
    }

    private void initViews(Bundle savedInstanceState) {
        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLoc != null) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DATA, currentLoc);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mMapView = (MapView) findViewById(R.id.mapView);
        // 此方法必须重写
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        UiSettings uiSettings = aMap.getUiSettings();
        //隐藏缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        // 添加移动地图事件监听器
        aMap.setOnCameraChangeListener(this);

        findViewById(R.id.btn_ensure).setOnClickListener(this);

        resultData = new ArrayList<>();
        lv_data = (ListView) findViewById(R.id.listView);
        searchResultAdapter = new SearchResultAdapter(this);
        searchResultAdapter.setData(resultData);
        lv_data.setAdapter(searchResultAdapter);
        lv_data.setOnItemClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(city)){
                    showToast("请先等待定位完成");
                    return;
                }
                Intent intent = new Intent(MapActivity.this, SearchLocationActivity2.class);
                intent.putExtra("city", city);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ensure:
                break;
            default:
        }
    }

    String city;

    /**
     * 返回定位结果的回调
     *
     * @param aMapLocation 定位结果
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == ERROR_CODE.ERROR_NONE) {
            //获取定位信息
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();
            mlocation = new LatLng(latitude, longitude);
            checkinpoint = mlocation;
            isLocationAction = true;
            city = aMapLocation.getCity();
            firstDesc = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
            if (!TextUtils.isEmpty(aMapLocation.getAoiName())) {
                firstDesc += aMapLocation.getAoiName();
            } else {
                firstDesc = firstDesc + aMapLocation.getPoiName() + "附近";
            }
            searchLatlonPoint = new LatLonPoint(latitude, longitude);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlocation, 17f));
            searchResultAdapter.setSelectedPosition(0);
            if (mcircle != null) {
                mcircle.setCenter(mlocation);
            } else {
                mcircle = aMap.addCircle(new CircleOptions().center(mlocation));
            }
            if (searchLatlonPoint != null) {
                resultData.clear();
                doSearchQuery(searchLatlonPoint);
                searchResultAdapter.notifyDataSetChanged();
            }
        } else {
            if(aMapLocation.getErrorCode() == 4){
                showToast("定位失败:网络连接异常");
            }else{
                showToast("定位失败:" + aMapLocation.getErrorCode() + ",请检查定位权限是否打开");
            }
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            LogUtils.e("AmapErr - " + errText);
        }
    }

    /**
     * 搜索周边poi
     *
     * @param centerpoint
     */
    private void doSearchQuery(LatLonPoint centerpoint) {
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(20);
        query.setPageNum(0);
        poisearch = new PoiSearch(this, query);
        //设置异步监听
        poisearch.setOnPoiSearchListener(this);
        //设置周边搜索的中心点以及半径
        poisearch.setBound(new PoiSearch.SearchBound(centerpoint, 500, true));
        //查询POI异步接口
        poisearch.searchPOIAsyn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        super.onDestroy();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.listView) {
            PoiItem poiItem1 = (PoiItem) searchResultAdapter.getItem(position);
            currentLoc = new LocationBean(poiItem1.getLatLonPoint().getLongitude(), poiItem1.getLatLonPoint().getLatitude(), searchResultAdapter.getAddress(position), searchResultAdapter.getAddress(position));
            if (position != searchResultAdapter.getSelectedPosition()) {
                PoiItem poiItem = (PoiItem) searchResultAdapter.getItem(position);
                LatLng curLatlng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                isItemClickAction = true;
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(curLatlng));
                searchResultAdapter.setSelectedPosition(position);
                searchResultAdapter.notifyDataSetChanged();
            }
        } else {
            List<LocationBean> dataLists = InputTipTask.getInstance(MapActivity.this).getBean();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(final CameraPosition cameraPosition) {
        searchLatlonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
        if (mcircle != null) {
            if (mcircle.contains(cameraPosition.target)) {
                checkinpoint = cameraPosition.target;
            }
        }

        if (!isLocationAction && !isItemClickAction) {
            LatLngEntity latLngEntity = new LatLngEntity(cameraPosition.target.latitude, cameraPosition.target.longitude);
            GeoCoderUtil.getInstance(MapActivity.this).geoAddress(latLngEntity, new GeoCoderUtil.GeoCoderAddressListener() {
                @Override
                public void onAddressResult(String result) {
                    firstDesc = result;
                    doSearchQuery(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude));
                }
            });

        } else {
            if (!isItemClickAction) {
                doSearchQuery(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude));
            }
        }
        if (isItemClickAction) {
            isItemClickAction = false;
        }
        if (isLocationAction) {
            isLocationAction = false;
        }
    }

    /**
     * 搜索Poi回调
     *
     * @param poiResult  搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {

        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getPois().size() > 0) {
                List<PoiItem> poiItems = poiResult.getPois();

                searchLatlonPoint = poiItems.get(0).getLatLonPoint();
                resultData.clear();
                PoiItem p = new PoiItem("ID", searchLatlonPoint, "[位置]", searchLatlonPoint.toString());
                p.setCityName(firstDesc);
                resultData.add(p);
                resultData.addAll(poiItems);
                searchResultAdapter.setSelectedPosition(0);
                searchResultAdapter.notifyDataSetChanged();
                currentLoc = new LocationBean(poiItems.get(0).getLatLonPoint().getLongitude(), poiItems.get(0).getLatLonPoint().getLatitude(), firstDesc, firstDesc);
            } else {
                showToast("无搜索结果");
            }
        } else {
            showToast("搜索失败，错误 " + resultCode);
        }
    }

    /**
     * ID搜索poi的回调
     *
     * @param poiItem    搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int resultCode) {

    }

    class PoiAdapter extends BaseAdapter {

        private List<LocationBean> datas = new ArrayList<>();

        private static final int RESOURCE = R.layout.app_list_item_poi;

        public PoiAdapter(Context context) {
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = getLayoutInflater().inflate(RESOURCE, null);
                vh.tv_title = (TextView) convertView.findViewById(R.id.address);
                vh.tv_text = (TextView) convertView.findViewById(R.id.addressDesc);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            LocationBean bean = (LocationBean) getItem(position);
            vh.tv_title.setText(bean.getTitle());
            vh.tv_text.setText(bean.getContent());
            return convertView;
        }

        private class ViewHolder {
            public TextView tv_title;
            public TextView tv_text;
        }

        public void setData(List<LocationBean> datas) {
            this.datas = datas;
            if (datas.size() > 0) {
                ll_poi.setVisibility(View.VISIBLE);
            } else {
                showToast("没有搜索结果");
                ll_poi.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            currentLoc = (LocationBean) data.getSerializableExtra(EXTRA_DATA);
            LatLng curLatlng = new LatLng(currentLoc.getLat(), currentLoc.getLng());
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(curLatlng));
        }
    }
}
