package com.nesscomputing.tracking.adapters;


import java.util.UUID;

import com.google.inject.Provider;
import com.nesscomputing.tracking.TrackingToken;

public class DummyTokenProvider implements Provider<TrackingToken>
{
    private UUID value;

    public DummyTokenProvider(final UUID value)
    {
        this.value = value;
    }

    public void setValue(final UUID value)
    {
        this.value = value;
    }

    @Override
    public TrackingToken get()
    {
        final TrackingToken token = new TrackingToken();
        token.setValue(value);
        return token;
    }
}
