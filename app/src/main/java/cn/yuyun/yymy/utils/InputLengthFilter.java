package cn.yuyun.yymy.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author
 * @desc
 * @date
 */
public class InputLengthFilter implements TextWatcher {

    private TextView tv;
    private int max = 200;

    public InputLengthFilter(TextView tv, int max){
        this.tv = tv;
        this.max = max;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.length() + "/" + max;
        tv.setText(text);
    }

}
