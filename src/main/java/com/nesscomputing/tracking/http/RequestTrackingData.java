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
package com.nesscomputing.tracking.http;


import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.nesscomputing.tracking.config.TrackingConfig;

@ThreadSafe
public class RequestTrackingData {
	@Inject
	private static TrackingConfig config;

    URI requestUri;
    Long requestStart;
    Long responseEnd;
    Integer responseCode;
    private LinkedList<RequestTrackingData> requests = Lists.newLinkedList();

    public synchronized URI getRequestUri() {
        return requestUri;
    }
    public synchronized Long getRequestStart() {
        return requestStart;
    }
    public synchronized Long getResponseEnd() {
        return responseEnd;
    }
    public synchronized Integer getResponseCode() {
        return responseCode;
    }
    public synchronized void setRequestUri(URI requestUri) {
        this.requestUri = requestUri;
    }
    public synchronized void setRequestStart(long requestStart) {
        this.requestStart = requestStart;
    }
    public synchronized void setResponseEnd(long responseEnd) {
        this.responseEnd = responseEnd;
    }
    public synchronized void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    public synchronized void addRequest(RequestTrackingData request) {
        Preconditions.checkArgument(request != this, "oh come on.  no recursive RequestTrackingData allowed.");
        Preconditions.checkArgument(config != null, "TrackingModule not installed, but you tried to use the request tracker anyway!");
        this.requests.add(request);
        if (this.requests.size() > config.maxRequestBranchesTracked()) {
        	this.requests.remove();
        }
    }
    public synchronized void setRequests(List<RequestTrackingData> requests) {
        Preconditions.checkArgument(!requests.contains(this), "oh come on.  no recursive RequestTrackingData allowed.");
        this.requests = Lists.newLinkedList(requests);
    }
    public synchronized List<RequestTrackingData> getRequests() {
        return requests;
    }
    @Override
    public synchronized String toString() {
        return String
                .format("RequestTrackingData [requestUri=%s, requestStart=%s, responseEnd=%s, responseCode=%s, requests=%s]",
                        requestUri, requestStart, responseEnd, responseCode,
                        requests);
    }
}
