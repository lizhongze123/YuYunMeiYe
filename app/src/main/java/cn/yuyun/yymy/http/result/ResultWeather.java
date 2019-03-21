package cn.yuyun.yymy.http.result;

import android.text.TextUtils;

import java.util.List;

import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date
 */
public class ResultWeather {


    public List<ResultsBean> results;

    public static class ResultsBean {
        /**
         * location : {"id":"C23NB62W20TF","name":"西雅图","country":"US","timezone":"America/Los_Angeles","timezone_offset":"-07:00"}
         * now : {"text":"多云","code":"4","temperature":"14","feels_like":"14","pressure":"1018","humidity":"76","visibility":"16.09","wind_direction":"西北","wind_direction_degree":"340","wind_speed":"8.05","wind_scale":"2","clouds":"90","dew_point":"-12"}
         * last_update : 2015-09-25T22:45:00-07:00
         */

        public LocationBean location;
        public NowBean now;
        public String last_update;

        public static class LocationBean {
            /**
             * id : C23NB62W20TF
             * name : 西雅图
             * country : US
             * timezone : America/Los_Angeles
             * timezone_offset : -07:00
             */

            public String id;
            public String name;
            public String country;
            public String timezone;
            public String timezone_offset;
        }

        public static class NowBean {
            /**
             * text : 多云
             * code : 4
             * temperature : 14
             * feels_like : 14
             * pressure : 1018
             * humidity : 76
             * visibility : 16.09
             * wind_direction : 西北
             * wind_direction_degree : 340
             * wind_speed : 8.05
             * wind_scale : 2
             * clouds : 90
             * dew_point : -12
             */

            public String text;


            public String code;


            public String temperature;
            public String feels_like;
            public String pressure;
            public String humidity;
            public String visibility;
            public String wind_direction;
            public String wind_direction_degree;
            public String wind_speed;
            public String wind_scale;
            public String clouds;
            public String dew_point;

            public int getCode() {
                if(!TextUtils.isEmpty(code)){
                    if(code.equals("0")){
                        return R.drawable.ic_sunny;
                    }else if(code.equals("1")){
                        return R.drawable.ic_sunny;
                    }else if(code.equals("2")){
                        return R.drawable.ic_sunny;
                    }else if(code.equals("3")){
                        return R.drawable.ic_sunny;
                    }else if(code.equals("4")){
                        return R.drawable.ic_cloudy;
                    }else if(code.equals("5")){
                        return R.drawable.ic_partly_cloudy;
                    }else if(code.equals("6")){
                        return R.drawable.ic_partly_cloudy;
                    }else if(code.equals("7")){
                        return R.drawable.ic_mostly_cloudy;
                    }else if(code.equals("8")){
                        return R.drawable.ic_mostly_cloudy;
                    }else if(code.equals("9")){
                        return R.drawable.ic_overcast;
                    }else if(code.equals("10")){
                        return R.drawable.ic_shower;
                    }else if(code.equals("11")){
                        return R.drawable.ic_shower;
                    }else if(code.equals("12")){
                        return R.drawable.ic_shower;
                    }else if(code.equals("13")){
                        return R.drawable.ic_shower;
                    }else if(code.equals("14")){
                        return R.drawable.ic_shower;
                    }else if(code.equals("15")){
                        return R.drawable.ic_shower;
                    }else if(code.equals("16")){
                        return R.drawable.ic_storm;
                    }else if(code.equals("17")){
                        return R.drawable.ic_storm;
                    }else if(code.equals("18")){
                        return R.drawable.ic_storm;
                    }else if(code.equals("19")){
                        return R.drawable.ic_shower;
                    }else if(code.equals("20")){
                        return R.drawable.ic_shower;
                    }else if(code.equals("21")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("22")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("23")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("24")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("25")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("26")){
                        return R.drawable.ic_dust;
                    }else if(code.equals("27")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("28")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("29")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("30")){
                        return R.drawable.ic_haze;
                    }else if(code.equals("31")){
                        return R.drawable.ic_haze;
                    }else if(code.equals("32")){
                        return R.drawable.ic_windy;
                    }else if(code.equals("33")){
                        return R.drawable.ic_windy;
                    }else if(code.equals("34")){
                        return R.drawable.ic_hurricane;
                    }else if(code.equals("35")){
                        return R.drawable.ic_hurricane;
                    }else if(code.equals("36")){
                        return R.drawable.ic_hurricane;
                    }else if(code.equals("37")){
                        return R.drawable.ic_snow_flurry;
                    }else if(code.equals("38")){
                        return R.drawable.ic_sunny;
                    }else {
                        return R.drawable.ic_unknown;
                    }
                }else {
                    return R.drawable.ic_unknown;
                }
            }
        }
    }
}
