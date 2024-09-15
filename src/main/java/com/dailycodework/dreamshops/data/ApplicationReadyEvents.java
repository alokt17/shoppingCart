package com.dailycodework.dreamshops.data;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class ApplicationReadyEvents extends ApplicationEvent {
    public ApplicationReadyEvents(Object source) {
        super(source);
    }

    public ApplicationReadyEvents(Object source, Clock clock) {
        super(source, clock);
    }
}
