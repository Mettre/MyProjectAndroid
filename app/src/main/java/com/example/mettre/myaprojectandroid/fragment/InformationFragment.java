package com.example.mettre.myaprojectandroid.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.UserBean;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.LoginUtils;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.cazaea.sweetalert.SweetAlertDialog;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Mettre on 2018/8/29.
 * 个人信息编写
 */

public class InformationFragment extends BaseMainFragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private final String TAG = "HMInformationFragment";
    public static final String IMAGE_FILE_NAME = "clip_temp.jpg";
    private final int RC_SETTINGS_SCREEN = 126;
    private final int RC_LOCATION_CONTACTS_PERM = 124;
    private final int LOCAL = 11;
    private final int CAMERA = 12;
    private final int CUT = 13;
    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ImageView icon_img;
    private Bitmap photo;

    private PopupWindow mPopupWindow;
    private Button ppBtnCamera;
    private Button ppBtnGallery;
    private Button ppBtnCancel;


    /**
     * 个人信息
     */
    private UserBean userBean;
    private Toolbar mToolbar;
    private TextView nickNameText;
    private TextView phoneText;
    private TextView genderText;
    private String nickName;
    private String gender;
    private Long iconAvatar;

    /**
     * 修改个人信息
     */
    private SubscriberOnNextListener getEditNickNameNext;
    private String inputEdit;//修改个人信息startForResult-不为空即修改过

    public static InformationFragment newInstance(UserBean userBean) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putSerializable("userBean", userBean);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        initView(view);
        initClickListener(view, R.id.icon_img, R.id.icon_img_right, R.id.out_btn, R.id.nickname_linearLayout,
               R.id.gender_linearLayout,R.id.right_btn);
        return view;
    }


    public void initView(View view) {
        userBean = (UserBean) getArguments().getSerializable("userBean");
        icon_img = view.findViewById(R.id.icon_img);
        genderText = view.findViewById(R.id.gender);
        phoneText = view.findViewById(R.id.phone_text);
        nickNameText = view.findViewById(R.id.nickName);

        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        mToolbar.setTitle("我的资料");
        getEditInformation();
    }

    /**
     * 修改个人信息
     */
    private void setEditUserInfo(final Boolean back) {

        getEditNickNameNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(final HttpResult3 response) {
                if (back) {
                    new SweetAlertDialog(_mActivity, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("")
                            .setContentText("修改成功")
                            .setConfirmText("关闭")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    pop();
                                }
                            })
                            .show();
                } else {
                    icon_img.setImageBitmap(photo);
                }
            }

            @Override
            public void onCompleted() {
                inputEdit = "";
            }

            @Override
            public void onError() {
                inputEdit = "";
                new SweetAlertDialog(_mActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("错误！")
                        .setContentText("用户信息修改错误！")
                        .setConfirmText("关闭")
                        .show();
            }

            @Override
            public void onSocketTimeout() {
                ToastUtils.showCenterToast("网络连接超时，请稍后重试", 200);
            }

            @Override
            public void onConnectException() {
                ToastUtils.showCenterToast("暂无网路连接，请稍后重试", 200);
            }
        };
        HttpMethods.getInstance().getEditUserInfo(new ProgressSubscriber(getEditNickNameNext, _mActivity), setEditInformation())
        ;
    }


    /**
     * 信息
     */
    private void getEditInformation() {
        if (userBean == null) {
            return;
        }
        nickName = userBean.getName();
        gender = userBean.getSex();

        phoneText.setText(userBean.getPhone());
        nickNameText.setText(TextUtils.isEmpty(nickName) ? "未设置" : nickName);
        nickNameText.setTextColor(TextUtils.isEmpty(nickName) ? getResources().getColor(R.color.gray_light) : getResources().getColor(R.color.monsoon));
        genderText.setText(TextUtils.isEmpty(gender) ? "未设置" : gender);
        genderText.setTextColor(TextUtils.isEmpty(gender) ? getResources().getColor(R.color.gray_light) : getResources().getColor(R.color.monsoon));
    }

    /**
     * 信息
     */
    private UserBean setEditInformation() {
        userBean.setName(nickName);
        userBean.setSex(gender);
        return userBean;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CUT:
                if (data != null) {
                    try {
                        setImageToHeadView(data);//设置图片框
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case LOCAL:
                cropRawPhoto(data.getData());
                break;
            case CAMERA:

                if (hasSdcard()) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(_mActivity, "没有SDCard!", Toast.LENGTH_LONG).show();
                }
                break;
            case RC_SETTINGS_SCREEN:
                Toast.makeText(_mActivity, "请在设置中开启拍照权限", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) throws FileNotFoundException {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            File nf = new File(Environment.getExternalStorageDirectory() + "/Ask");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory() + "/Ask", "okkk.jpg");
            FileOutputStream out;
            try {//打开输出流 将图片数据填入文件中
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //把裁剪的数据填入里面

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CUT);

    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            inputEdit = data.getString("inputEdit");
            switch (requestCode) {
                case 1000:
                    nickName = inputEdit;
                    nickNameText.setText(inputEdit);
                    nickNameText.setTextColor(TextUtils.isEmpty(inputEdit) ? getResources().getColor(R.color.gray_light) : getResources().getColor(R.color.monsoon));
                    break;
                case 4000:
                    gender = inputEdit;
                    genderText.setText(inputEdit);
                    genderText.setTextColor(TextUtils.isEmpty(inputEdit) ? getResources().getColor(R.color.gray_light) : getResources().getColor(R.color.monsoon));
                    break;
            }
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        if (!TextUtils.isEmpty(inputEdit)) {
            new SweetAlertDialog(_mActivity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("提示")
                    .setContentText("是否保存修改信息")
                    .setCancelText("取消")
                    .setConfirmText("确定")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            pop();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            setEditUserInfo(true);

                        }
                    })
                    .show();
        } else {
            pop();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_img:
                showPopupWindow(v);
                break;
            case R.id.icon_img_right:
                showPopupWindow(v);
                break;
            case R.id.out_btn:
                LoginUtils.getInstance().signOutRemoveToken();
                Bundle bundle = new Bundle();
                setFragmentResult(RESULT_OK, bundle);
                pop();
                break;
            case R.id.nickname_linearLayout:
                startForResult(EditNickNameFragment.newInstance(nickName, 1), 1000);
                break;
            case R.id.gender_linearLayout:
                startForResult(EditNickNameFragment.newInstance(gender, 4), 4000);
                break;
            case R.id.right_btn:
                if (!TextUtils.isEmpty(inputEdit)) {
                    setEditUserInfo(true);
                } else {
                    pop();
                }
                break;
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha, float bgDim) {
        WindowManager.LayoutParams lp = _mActivity.getWindow().getAttributes();
        lp.dimAmount = bgDim;
        lp.alpha = bgAlpha; //0.0-1.0
        _mActivity.getWindow().setAttributes(lp);

        _mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 获取file的时候如果没有路径就重新创建
     *
     * @return
     */
    private File getFile() {
        File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    /**
     * 初始化popWindow
     */
    private void initPopWindow(View popView, PopupWindow popupWindow) {

        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        ppBtnCamera = (Button) popView.findViewById(R.id.pp_btn_camera);
        ppBtnGallery = (Button) popView.findViewById(R.id.pp_btn_gallery);
        ppBtnCancel = (Button) popView.findViewById(R.id.pp_btn_cancel);

    }


    private void showPopupWindow(final View view) {

        // 一个自定义的布局，作为显示的内容
        final View popView = LayoutInflater.from(_mActivity).inflate(R.layout.pop_photo, null);
        mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //初始化
        initPopWindow(popView, mPopupWindow);

        // 设置按钮的点击事件

        ppBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();

                //权限判断
                if (EasyPermissions.hasPermissions(_mActivity, perms)) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFile()));
                    startActivityForResult(intent, CAMERA);
                } else {
                    // Ask for both permissions
                    EasyPermissions.requestPermissions(_mActivity, "需要拍照相关权限", RC_LOCATION_CONTACTS_PERM, perms);
                }


            }
        });
        ppBtnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                Intent intentFromGallery;
                if (android.os.Build.VERSION.SDK_INT >= 19) { // 判断是不是4.4
                    intentFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                } else {
                    intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
                }
                intentFromGallery.setType("image/*"); // 设置文件类型
                startActivityForResult(intentFromGallery, LOCAL);
            }
        });

        ppBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        // 设置外部可点击
        mPopupWindow.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = popView.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        mPopupWindow.dismiss();
                    }
                }
                return true;
            }
        });

        mPopupWindow.setTouchable(true);

        //获取焦点
        mPopupWindow.setFocusable(true);

//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 设置弹出窗体的背景
//        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindow.dismiss();
            }
        });

        backgroundAlpha(0.5f, 1f);//透明度
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //添加pop窗口关闭事件
        mPopupWindow.setOnDismissListener(new InformationFragment.poponDismissListener());

        mPopupWindow.update();
        if (!mPopupWindow.isShowing()) {
            //设置显示位置
            mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFile()));
        startActivityForResult(intent, CAMERA);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f, 0.1f);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
