package com.nesscomputing.tracking.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import com.nesscomputing.scopes.threaddelegate.ThreadDelegated;
import com.nesscomputing.scopes.threaddelegate.concurrent.ThreadDelegatingDecorator;

/**
 * Static factory class for composing tracking-token awareness onto given
 * Executors.  Composed classes delegate functionality to the given executor after
 * ensuring that the {@link TrackingData} is correctly propagated across thread boundaries<br/>
 * The static versions use a default tracking manager which cannot create new tokens.  Inject this
 * and use the {@link #wrap} methods if you want to choose the tracking manager.
 *
 * All of this code is now obsolete because tracking uses the {@link ThreadDelegated} scope to perform
 * the same magic. Use the {@link ThreadDelegatingDecorator} instead.
 *
 * @author steven
 *
 * @deprecated Use the {@link ThreadDelegatingDecorator}.
 */
@Deprecated
public class TrackedExecutors
{
	/** Wrap an {@link Executor} to forward the tracking token. */
	public Executor wrap(Executor wrapped) {
		return ThreadDelegatingDecorator.wrapExecutor(wrapped);
	}

	/** Wrap an {@link ExecutorService} to forward the tracking token. */
	public ExecutorService wrap(ExecutorService wrapped) {
        return ThreadDelegatingDecorator.wrapExecutorService(wrapped);
	}

	/** Wrap an {@link Executor} to forward the tracking token. */
	public static Executor of(Executor wrapped) {
        return ThreadDelegatingDecorator.wrapExecutor(wrapped);
	}
	/** Wrap an {@link ExecutorService} to forward the tracking token. */
	public static ExecutorService of(ExecutorService wrapped) {
        return ThreadDelegatingDecorator.wrapExecutorService(wrapped);
	}
}
