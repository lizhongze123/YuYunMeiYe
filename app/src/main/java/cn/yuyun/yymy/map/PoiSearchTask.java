package cn.yuyun.yymy.map;

import android.content.Context;

import com.amap.api.maps.AMapUtils;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;

import cn.lzz.utils.LogUtils;

public class PoiSearchTask implements PoiSearch.OnPoiSearchListener {

	private static PoiSearchTask mInstance;
	private MapActivity.PoiAdapter mAdapter;
	private PoiSearch mSearch;
	private Context mContext;

	private PoiSearchTask(Context context){
		this.mContext = context;
	}

	public static PoiSearchTask getInstance(Context context){
		if(mInstance == null){
			synchronized (PoiSearchTask.class) {
				if(mInstance == null){
					mInstance = new PoiSearchTask(context);
				}
			}
		}
		return mInstance;
	}

	public PoiSearchTask setAdapter(MapActivity.PoiAdapter adapter){
		this.mAdapter = adapter;
		return this;
	}

	public void onSearch(String key, String city,double lat,double lng){
		// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		PoiSearch.Query query = new PoiSearch.Query(key, "", city);
		mSearch = new PoiSearch(mContext, query);
		//设置周边搜索的中心点以及半径
		mSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lng), 2000));
		//设置异步监听
		mSearch.setOnPoiSearchListener(this);
		//查询POI异步接口
		mSearch.searchPOIAsyn();
	}

	@Override
	public void onPoiSearched(PoiResult poiResult, int rCode) {
		if(rCode == 1000) {
			if (poiResult != null && poiResult.getQuery() != null) {
				ArrayList<LocationBean> datas = new ArrayList<>();
				ArrayList<PoiItem> items = poiResult.getPois();
				for (PoiItem item : items) {
					//获取经纬度对象
					LatLonPoint llp = item.getLatLonPoint();
					double lon = llp.getLongitude();
					double lat = llp.getLatitude();
					//获取标题
					String title = item.getTitle();
					//获取内容
					String text = item.getCityName() + item.getAdName() + item.getSnippet();
					datas.add(new LocationBean(lon, lat, title, text));
				}
				mAdapter.setData(datas);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onPoiItemSearched(PoiItem poiItem, int i) {

	}
}
