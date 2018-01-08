package com.william.simpletalk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.william.common.Common;
import com.william.common.app.BaseActivity;
import com.william.common.app.BaseApplication;
import com.william.common.widget.PortraitView;
import com.william.flow.model.card.UserCard;
import com.william.simpletalk.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactAddActivity extends BaseActivity {

    @BindView(R.id.txt_back)
    TextView txtBack;
    @BindView(R.id.txt_send)
    TextView txtSend;
    @BindView(R.id.im_portrait)
    PortraitView imPortrait;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.edit_remark)
    EditText editRemark;
    @BindView(R.id.im_delete)
    ImageView imDelete;
    private boolean isSend;
    private UserCard card;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_contact_add;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        if (bundle.containsKey("isSend") && bundle.containsKey("userCard")) {
            isSend = bundle.getBoolean("isSend");
            card = bundle.getParcelable("userCard");
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        editRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0)
                    imDelete.setVisibility(View.INVISIBLE);
                else
                    imDelete.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        txtName.setText(card.getName());
        txtDesc.setText(card.getDesc());
        imPortrait.setup(Glide.with(this),card.getPortrait());
    }

    @OnClick({R.id.txt_back, R.id.txt_send, R.id.im_delete, R.id.im_portrait})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_portrait:
                break;
            case R.id.txt_back:
                finish();
                break;
            case R.id.txt_send:
                String content = editContent.getText().toString().trim();
                String remark = editRemark.getText().toString().trim();
                if (TextUtils.isEmpty(content) || TextUtils.isEmpty(remark)) {
                    BaseApplication.showToast(R.string.toast_contact_content_nullable);
                } else {
                    isSend = true;
                    Intent intent = new Intent();
                    intent.putExtra("isSend", isSend);
                    intent.putExtra("content", content);
                    intent.putExtra("remark", remark);
                    setResult(Common.Constance.RESULT_FOLLOW, intent);
                    finish();
                }
                break;
            case R.id.im_delete:
                editRemark.setText("");
                break;
            default:
                break;
        }
    }
}
