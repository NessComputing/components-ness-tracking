/**
 * Copyright (C) 2012 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nesscomputing.tracking.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

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
