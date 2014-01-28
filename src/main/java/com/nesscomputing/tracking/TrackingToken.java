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
package com.nesscomputing.tracking;

import java.util.UUID;

import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedContext.ScopeEvent;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedContext.ScopeListener;

import org.apache.log4j.MDC;

/**
 * Represents the tracking UUID. This object is in the ThreadDelegated scope and will
 * be notified whenever it gets handed onto another thread to allow updating of
 * that's thread MDC information.
 */
public class TrackingToken implements ScopeListener
{
    public static final String MDC_TRACKING_KEY = "track";

    private UUID value = null;

    public void setValue(final UUID value)
    {
        this.value = value;
        if (value != null) {
            MDC.put(MDC_TRACKING_KEY, value);
        }
    }

    public UUID getValue()
    {
        return value;
    }

    @Override
    public void event(final ScopeEvent event)
    {
        switch (event) {
            case ENTER:
                if (value != null) {
                    MDC.put(MDC_TRACKING_KEY, value);
                }
                break;
            case LEAVE:
                MDC.remove(MDC_TRACKING_KEY);
                break;
            default:
                throw new IllegalStateException("Unknown Event: " + event);
        }
    }
}
