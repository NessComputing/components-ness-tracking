package io.trumpet.tracking.adapters;

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
