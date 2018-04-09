/*
 * Copyright 2018 CMPUT301W18T18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import android.location.Location;

/**
 * TaskLocation is the Location class
 * used in task to make Elasticsearch
 * location queries work
 *
 * @see Location
 */
public class TaskLocation {
    // NOTE These variables probably need to be named exactly
    // "lat" and "lon" in order to be searchable
    private double lat;
    private double lon;

    /**
     * create a TaskLocation in from a Location
     * @param location the Location
     */
    public TaskLocation(Location location) {
        this.lat = location.getLatitude();
        this.lon = location.getLongitude();
    }

    /**
     * create a TaskLocation from coordinates.
     * @param latitude the latitude
     * @param longitude the longitute
     */
    public TaskLocation(Double latitude, double longitude) {
        this.lat = latitude;
        this.lon = longitude;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return lat;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return lon;
    }
}
