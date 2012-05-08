package io.trumpet.tracking;

import java.util.UUID;

import org.apache.log4j.MDC;

import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedContext.ScopeEvent;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedContext.ScopeListener;

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
        }
    }
}
