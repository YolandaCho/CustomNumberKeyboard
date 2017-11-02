package com.yolanda.cho.customnumkeyboard;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import com.yolanda.cho.customnumkeyboard.widget.VirtualKeyboardView;
import java.lang.reflect.Method;
/**
 * Created by YolandaCho on 2017/11/2.
 * 自定义数字键盘
 */

public class MainActivity extends Activity {
    public VirtualKeyboardView virtualKeyboardView;
    public EditText etPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    public void initView()
    {
        virtualKeyboardView = (VirtualKeyboardView) findViewById(R.id.virtualKeyboardView);
        etPhone = (EditText) findViewById(R.id.et_phone);
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            etPhone.setInputType(InputType.TYPE_NULL);
        } else {
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(etPhone, false);
                etPhone.setCursorVisible(true);
                virtualKeyboardView.bindInputView(etPhone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置手机号码格式:  132 3232 3232
     */
    public void initListener() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence == null || charSequence.length() == 0)
                    return;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < charSequence.length(); i++) {
                    if (i != 3 && i != 8 && charSequence.charAt(i) == ' ') {
                        continue;
                    } else {
                        stringBuilder.append(charSequence.charAt(i));
                        if ((stringBuilder.length() == 4 || stringBuilder.length() == 9)
                                && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                            stringBuilder.insert(stringBuilder.length() - 1, ' ');
                        }
                    }
                }
                if (!stringBuilder.toString().equals(charSequence.toString())) {
                    int index = start + 1;
                    if (stringBuilder.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    etPhone.setText(stringBuilder.toString());
                    etPhone.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
