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

import javax.annotation.Nullable;

/**
 * Represents an entity that provides and/or consumes the tracking information.
 * This can be either an ingressing entity (e.g. ServletFilter or Container plugin)
 * or an egressing entity (adapter to HttpClient etc.).
 */
public interface TrackingAdapter
{
    /**
     * Return a tracking token from the entity. E.g. the servlet filter returns the
     * information from the tracking header. May return null.
     */
    @Nullable UUID getTrackingUUID();
}
