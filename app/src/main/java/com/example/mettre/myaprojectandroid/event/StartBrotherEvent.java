package com.example.mettre.myaprojectandroid.event;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Mettre on 2018/8/14.
 */

public class StartBrotherEvent {

    public SupportFragment targetFragment;
    public int EventType;

    public StartBrotherEvent(SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
    }

    public StartBrotherEvent(int EventType) {
        this.EventType = EventType;
    }
}
