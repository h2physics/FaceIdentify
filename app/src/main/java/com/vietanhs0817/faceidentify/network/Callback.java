package com.vietanhs0817.faceidentify.network;

import com.vietanhs0817.faceidentify.model.Actor;

import java.util.List;

/**
 * Created by YukiNoHara on 11/1/2017.
 */

public interface Callback {
    void onSuccess(Actor actors);
    void onFailed();
}
