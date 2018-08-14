package com.example.mettre.myaprojectandroid.progress;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.mettre.myaprojectandroid.R;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author Mettre
 * @version [版本号, 2016/11/28 14:13]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ProgressDialog extends Dialog {

    View customView;

    Context context;

    public ProgressDialog(Context context) {
        super(context, R.style.DialogStyle);
        this.context = context;
    }


    /**
     * 展示
     */
    public void show() {
        super.show();
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        customView = mInflater.inflate(R.layout.view_progress, null);
        setContentView(customView);

        // 设置他的宽度

//        WindowManager wm = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);

        //int screenWidth = dm.widthPixels;

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.alpha = 0.9f;
        //lp.width = screenWidth * 4 / 5;
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        //setCancelable(false);
        setCanceledOnTouchOutside(false);

//        ButterKnife.bind(this);


    }

    public View getRootView() {
        return customView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    public interface SubmitDoing {
        void submitDoing(String strSex);
    }
}
