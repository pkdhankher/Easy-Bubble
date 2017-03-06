package com.eworl.easybubble.activities;

import com.eworl.easybubble.db.Program;

import java.util.List;

/**
 * Created by Dhankher on 3/4/2017.
 */

public interface CallBack {
    void onWorkComplited(List<Program> allItems);
}
