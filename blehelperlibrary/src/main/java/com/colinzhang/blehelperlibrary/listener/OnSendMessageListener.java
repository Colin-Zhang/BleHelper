package com.colinzhang.blehelperlibrary.listener;

/**
 * Created by zhang on 2018/12/12.
 * 消息发送监听
 */

public interface OnSendMessageListener extends IErrorListener, IConnectionLostListener {

    void onSuccess(int status, String response);
}