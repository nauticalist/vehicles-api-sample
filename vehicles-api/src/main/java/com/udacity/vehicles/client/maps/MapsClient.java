package com.udacity.vehicles.client.maps;

import com.udacity.vehicles.domain.Location;

public interface MapsClient {
    Location getAddress(Location location);

}
