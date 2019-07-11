package com.colinzhang.blehelperlibrary.listener;

/**
 * Created by zhang on 2018/12/12.
 * 连接断开监听
 */

public interface IConnectionLostListener {
    void onConnectionLost(Exception e);
}
