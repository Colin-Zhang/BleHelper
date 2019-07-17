package com.colinzhang.blehelperlibrary.listener;

/**
 *Created by zhang on 2018/12/12.
 * 接收消息监听
 */

public interface OnReceiveMessageListener{
    /**
     * call when have some response
     * @param s
     */
    void onReceiveMessage(byte[] s);
}

