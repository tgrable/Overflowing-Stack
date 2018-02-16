package com.grable.overflowingstack.vone.interfaces;

import java.util.List;

/**
 * Created by timgrable on 10/31/17.
 */

public interface ObserverListener {
    void observerOnSubscribe();
    void observerOnNext(List<?> list);
    void observerOnError(Throwable e);
    void observerOnComplete();
}
