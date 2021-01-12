package com.example.refreshlayout_harmonyos;

import ohos.agp.components.AttrSet;
import ohos.agp.components.ComponentContainer;
import ohos.app.Context;

public abstract class HeadBaseComponent extends ComponentContainer {


    public enum RefreshState {
        /**
         * 初始状态
         */
        STATE_INIT,
        /**
         * 下拉刷新的头部可见
         */
        STATE_VISIBLE,
        /**
         * 正在刷新的状态
         */
        STATE_REFRESH,
        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER,
        /**
         * 超出刷新位置松开手后的状态
         */
        STATE_OVER_RELEASE

    }

    //默认状态为  初始化状态
    protected RefreshState mState = RefreshState.STATE_INIT;
    /**
     * 触发下拉刷新时的最小高度，当刚好下拉到这个距离，那就直接刷新，
     * 如果下拉的距离超过了这个距离，那就先滚动到这个距离，然后才开始刷新
     */
    public int mPullRefreshHeight;
    /**
     * 最小阻尼，用户越往下拉，越不跟手
     */
    public float minDamp = 1.6f;
    /**
     * 最大阻尼
     */
    public float maxDamp = 2.2f;

    public HeadBaseComponent(Context context) {
        this(context, null);
    }

    public HeadBaseComponent(Context context, AttrSet attrSet) {
        this(context, attrSet, "");
    }

    public HeadBaseComponent(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init();
    }

    public abstract void init();


    public abstract void onHeadVisible();


    public abstract void onHeadOver();


    public abstract void onRefresh();


    public abstract void onFinish();

    public void setState(RefreshState state) {
        this.mState = state;
    }

    public RefreshState getState() {
        return mState;
    }

}
