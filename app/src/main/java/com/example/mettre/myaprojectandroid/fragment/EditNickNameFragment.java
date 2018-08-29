package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.utils.LoginUtils;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;

/**
 * Created by app on 2017/10/20.
 * 修改信息
 */

public class EditNickNameFragment extends BaseMainFragment {

    private Toolbar mToolbar;
    private TextView right_text;
    private String nickName;
    private EditText nicknameEdt;
    private RadioGroup radioGroup;
    private TextView len_num;
    private TextView promptText;
    private int type;//1:昵称 2：真实姓名 3：e-mall 4:性别

    public static EditNickNameFragment newInstance(String nickName, int type) {
        EditNickNameFragment fragment = new EditNickNameFragment();
        Bundle args = new Bundle();
        args.putString("nickName", nickName);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_nick_name, container, false);
        initView(view);
        initClickListener(view, R.id.right_btn);
        return view;
    }

    protected void initView(View view) {
        nickName = getArguments().getString("nickName");
        type = getArguments().getInt("type");
        nicknameEdt = view.findViewById(R.id.nickname_edt);
        len_num = view.findViewById(R.id.len_num);
        promptText = view.findViewById(R.id.prompt_text);
        radioGroup = view.findViewById(R.id.group_gender);
        right_text = view.findViewById(R.id.right_btn);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));

        initToolbarNav(mToolbar);
        mToolbar.setTitle("我的资料");
        switch (type) {
            case 1:
                mToolbar.setTitle("编辑昵称");
                break;
            case 4:
                mToolbar.setTitle("编辑性别");
                break;
        }
        initClickListener(view, R.id.right_btn);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        nicknameEdt.setText(nickName);

        right_text.setText("确定");
        right_text.setBackgroundResource(R.drawable.bg_gray2);
        right_text.setVisibility(View.VISIBLE);

        switch (type) {
            case 1:
                nicknameEdt.setHint("请输入昵称");
                promptText.setText("限制4-10位字符");
                LimitMaxLine(10);
                if (!TextUtils.isEmpty(nickName)) {
                    len_num.setText(nickName.length() + "/" + 10);
                }
                break;
            case 2:
                nicknameEdt.setHint("请输入真实姓名");
                promptText.setText("限制2-6位字符");
                LimitMaxLine(6);
                if (!TextUtils.isEmpty(nickName)) {
                    len_num.setText(nickName.length() + "/" + 6);
                }
                break;
            case 3:
                nicknameEdt.setHint("请输入您的邮箱");
                promptText.setText("请输入电子邮箱地址");
//                nicknameEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                break;
            case 4:
                radioGroup.setVisibility(View.VISIBLE);
                nicknameEdt.setVisibility(View.GONE);
                promptText.setVisibility(View.GONE);
                len_num.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(nickName)) {
                    switch (nickName) {
                        case "男":
                            radioGroup.check(R.id.gender_male);
                            break;
                        case "女":
                            radioGroup.check(R.id.gender_female);
                            break;
                    }
                }
                break;
        }
    }


    private void LimitMaxLine(final int maxLen) {
        len_num.setText("0/" + maxLen);
        nicknameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable editable = nicknameEdt.getText();
                int len = editable.length();

                len_num.setText(len + "/" + maxLen);
                if (len > maxLen) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxLen);
                    nicknameEdt.setText(newStr);
                    editable = nicknameEdt.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:
                if (type == 4) {
                    Bundle bundle = new Bundle();
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.gender_male:
                            bundle.putString("inputEdit", "男");
                            break;
                        case R.id.gender_female:
                            bundle.putString("inputEdit", "女");
                            break;
                    }
                    setFragmentResult(RESULT_OK, bundle);
                    pop();
                } else {
                    if (TextUtils.isEmpty(nicknameEdt.getText().toString())) {
                        ToastUtils.showCenterToast("输入内容不能为空", 200);
                    } else if (3 == type && !LoginUtils.checkEmail(nicknameEdt.getText().toString())) {
                        ToastUtils.showCenterToast("请输入正确的邮箱地址", 200);
                    } else if (TextUtils.isEmpty(nickName) || !nickName.equals(nicknameEdt.getText().toString())) {
                        Bundle bundle = new Bundle();
                        bundle.putString("inputEdit", nicknameEdt.getText().toString());
                        setFragmentResult(RESULT_OK, bundle);
                        pop();
                    } else {
                        pop();
                    }
                }
                break;
        }
    }
}
