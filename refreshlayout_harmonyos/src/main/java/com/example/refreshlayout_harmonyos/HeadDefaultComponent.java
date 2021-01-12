package com.example.refreshlayout_harmonyos;

import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

public class HeadDefaultComponent extends HeadBaseComponent {

    private Image mImage;
    private Text mText;
    private AnimatorProperty mAnimator;

    public HeadDefaultComponent(Context context) {
        this(context, null);
    }

    public HeadDefaultComponent(Context context, AttrSet attrSet) {
        this(context, attrSet, "");

    }

    public HeadDefaultComponent(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    @Override
    public void init() {
        LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_head_default_layout, this, true);
        mImage = (Image) findComponentById(ResourceTable.Id_image);
        mText = (Text) findComponentById(ResourceTable.Id_text);
    }

    @Override
    public void onHeadVisible() {
        mText.setText("下拉刷新");
    }

    @Override
    public void onHeadOver() {
        mText.setText("松开刷新");
    }

    @Override
    public void onRefresh() {
        mText.setText("正在刷新...");
        mAnimator = mImage.createAnimatorProperty();
        mAnimator.setDuration(2000).rotate(360).setLoopedCount(AnimatorProperty.INFINITE).setTarget(mImage).start();
    }

    @Override
    public void onFinish() {
        mText.setText("刷新成功");
        mAnimator.cancel();
    }
}
