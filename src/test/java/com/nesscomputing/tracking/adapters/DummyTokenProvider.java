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
