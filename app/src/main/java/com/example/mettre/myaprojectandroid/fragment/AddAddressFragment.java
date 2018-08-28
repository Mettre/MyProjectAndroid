package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.bean.City;
import com.example.mettre.myaprojectandroid.dialog.BottomDialog;
import com.example.mettre.myaprojectandroid.http.HttpMethods3;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.LoginUtils;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;
import com.google.gson.Gson;
import com.yiguo.adressselectorlib.AddressSelector;
import com.yiguo.adressselectorlib.CityInterface;
import com.yiguo.adressselectorlib.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by app on 2017/7/21.
 * 新增收货地址
 */

public class AddAddressFragment extends BaseMainFragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView city_choice;// 请选择
    private TextView address_save;
    private EditText name_address;
    private EditText phone_address;
    private EditText detailed_address;
    private CheckBox checkbox;

    /**
     * 临时存储
     */
    private String province2;
    private String city2;
    private String district2;

    private AddressBean item;
    private String addressId;//地址id
    private String province;
    private String city;
    private String district;

    private ArrayList<City> cities1 = new ArrayList<>();//省
    private ArrayList<City> cities2 = new ArrayList<>();//市
    private ArrayList<City> cities3 = new ArrayList<>();//区

    /**
     * 添加收货地址
     */
    private SubscriberOnNextListener getAddAddressNext;
    private BottomDialog bottomDialog;


    public static AddAddressFragment newInstance(String addressId, AddressBean addressBean) {
        AddAddressFragment fragment = new AddAddressFragment();
        Bundle args = new Bundle();
        args.putString("addressId", addressId);
        args.putSerializable("item", addressBean);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddAddressFragment newInstance() {
        return new AddAddressFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        city_choice.setOnClickListener(this);
        address_save.setOnClickListener(this);
    }

    public void initView(View view) {
        addressId = getArguments().getString("addressId");
        item = (AddressBean) getArguments().getSerializable("item");

        mToolbar = view.findViewById(R.id.toolbar);
        city_choice = view.findViewById(R.id.city_choice);
        address_save = view.findViewById(R.id.address_save);
        name_address = view.findViewById(R.id.name_address);
        phone_address = view.findViewById(R.id.phone_address);
        detailed_address = view.findViewById(R.id.detailed_address);
        checkbox = view.findViewById(R.id.checkbox);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        if (TextUtils.isEmpty(addressId)) {
            mToolbar.setTitle("新建地址");
        } else {
            mToolbar.setTitle("修改地址");
        }
        /**
         * 修改信息
         */
        if (TextUtils.isEmpty(addressId) && item != null) {
            editContent(item);
        }
        //拿到本地JSON 并转成String
        try {
            JSONArray jsonArray = new JSONArray(getString(R.string.cities1));
            for (int i = 0; i < jsonArray.length(); i++) {
                cities1.add(new Gson().fromJson(jsonArray.get(i).toString(), City.class));
            }
            JSONArray jsonArray2 = new JSONArray(getString(R.string.cities2));
            for (int i = 0; i < jsonArray2.length(); i++) {
                cities2.add(new Gson().fromJson(jsonArray2.get(i).toString(), City.class));
            }
            JSONArray jsonArray3 = new JSONArray(getString(R.string.cities3));
            for (int i = 0; i < jsonArray3.length(); i++) {
                cities3.add(new Gson().fromJson(jsonArray3.get(i).toString(), City.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 部分城市选择show dialog
     */
    private void showDialog() {

        bottomDialog = new BottomDialog(3, _mActivity, cities1, new OnItemClickListener() {
            @Override
            public void itemClick(AddressSelector addressSelector, CityInterface cityInterface, int tabPosition) {
                switch (tabPosition) {
                    case 0:
                        province2 = cityInterface.getCityName();
                        addressSelector.setCities(cities2);
                        break;
                    case 1:
                        city2 = cityInterface.getCityName();
                        addressSelector.setCities(cities3);
                        break;
                    case 2:
                        district2 = cityInterface.getCityName();
                        city_choice.setText(province2 + city2 + district2);
                        bottomDialog.dismiss();
                        break;
                }
            }
        }, new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()) {
                    case 0:
                        addressSelector.setCities(cities1);
                        break;
                    case 1:
                        addressSelector.setCities(cities2);
                        break;
                    case 2:
                        addressSelector.setCities(cities3);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });
        bottomDialog.showAtLocation(_mActivity.findViewById(R.id.main_address), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.4f);
        bottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    /**
     * 屏幕透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = _mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            _mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            _mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        _mActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
//        //新建地址选择省份回来
//        if (requestCode == 2000 && resultCode == RESULT_OK && data != null) {
//            CommunityBean communityBean = (CommunityBean) data.getSerializable("communityBean");
//            String detailedAddress = communityBean.getAddress() + communityBean.getName();
//            detailed_address.setText(detailedAddress);
//            if (!detailedAddress.isEmpty()) {
//                detailed_address.setSelection(detailedAddress.length());//将光标移至文字末尾
//            }
//        }
    }

    /**
     * 修改地址信息
     */
    private void editContent(AddressBean item) {
        name_address.setText(item.getRecipientName());
        phone_address.setText(item.getRecipientPhoneNumber());
        city_choice.setText(item.getProvince() + " " + item.getCity() + " " + item.getCounty());
        detailed_address.setText(item.getAddress());
        checkbox.setChecked(item.isDefaulted());
        province = item.getProvince();
        city = item.getCity();
        district = item.getCounty();
    }

    private AddressBean submitAddress() {
        AddressBean request = new AddressBean();
        if (!TextUtils.isEmpty(addressId)) {
            request.setId(Integer.parseInt(addressId));
        }
        request.setRecipientName(name_address.getText().toString());
        request.setRecipientPhoneNumber(phone_address.getText().toString());
        request.setAddress(detailed_address.getText().toString());
        request.setCity(city);
        request.setProvince(province);
        request.setCounty(district);
        request.setDefaulted(checkbox.isChecked());
        return request;
    }

    /**
     * 添加收货地址
     */
    private void getAddCartInfo(AddressBean request, boolean type) {

        getAddAddressNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(HttpResult3 response) {
                new SweetAlertDialog(_mActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(TextUtils.isEmpty(addressId) ? "添加地址成功!" : "修改地址成功!")
                        .setConfirmText("关闭")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                Bundle bundle = new Bundle();
                                setFragmentResult(RESULT_OK, bundle);
                                pop();
                            }
                        })
                        .show();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSocketTimeout() {
                ToastUtils.showCenterToast("网络连接超时，请稍后重试", 200);
            }

            @Override
            public void onConnectException() {
                ToastUtils.showCenterToast("暂无网络连接，请确认设备连接网络", 200);
            }
        };
        HttpMethods3.getInstance().AddAddressRequest(new ProgressSubscriber(getAddAddressNext, _mActivity), request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_save:
                if (LoginUtils.getInstance().AddAddress(name_address, phone_address, city_choice, detailed_address)) {
                    getAddCartInfo(submitAddress(), TextUtils.isEmpty(addressId));
                }
                break;
            case R.id.city_choice:
                showDialog();
                break;
        }
    }
}
