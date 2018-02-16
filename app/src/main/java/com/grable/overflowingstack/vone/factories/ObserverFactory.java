package com.grable.overflowingstack.vone.factories;

import com.grable.overflowingstack.vone.interfaces.ObserverListener;
import com.grable.overflowingstack.vone.models.OSResponse;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by timgrable on 10/31/17.
 *
 * I was originally planning on making multiple api calls so I broke this out into its own class but then
 * found the search/advanced api call that returned all the data i need.
 *
 *
 */

public class ObserverFactory {

    public static <T> void createObserverObj(io.reactivex.Observable<T> observable, final ObserverListener listener) {
        observable.subscribe(new Observer<T>() {

            /**
             * Provides the Observer with the means of cancelling (disposing) the
             * connection (channel) with the Observable in both
             * synchronous (from within {@link #onNext(Object)}) and asynchronous manner.
             *
             * @param d the Disposable instance whose {@link Disposable#dispose()} can
             *          be called anytime to cancel the connection
             * @since 2.0
             */
            @Override
            public void onSubscribe(Disposable d) {
                listener.observerOnSubscribe();
            }

            /**
             * Provides the Observer with a new item to observe.
             * <p>
             * The {@link Observable} may call this method 0 or more times.
             * <p>
             * The {@code Observable} will not call this method again after it calls either {@link #onComplete} or
             * {@link #onError}.
             *
             * @param t the item emitted by the Observable
             */
            @Override
            public void onNext(T t) {
                OSResponse obj = (OSResponse)t;
                obj.getQuotaRemaining();
                listener.observerOnNext(obj.getItems());
            }

            /**
             * Notifies the Observer that the {@link Observable} has experienced an error condition.
             * <p>
             * If the {@link Observable} calls this method, it will not thereafter call {@link #onNext} or
             * {@link #onComplete}.
             *
             * @param e the exception encountered by the Observable
             */
            @Override
            public void onError(Throwable e) {
                listener.observerOnError(e);
            }

            /**
             * Notifies the Observer that the {@link Observable} has finished sending push-based notifications.
             * <p>
             * The {@link Observable} will not call this method if it calls {@link #onError}.
             */
            @Override
            public void onComplete() {
                listener.observerOnComplete();
            }
        });
    }

}
