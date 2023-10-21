package com.example.mainservice.mapper;

import com.example.mainservice.model.Location;
import com.example.mainservice.model.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public LocationDto mapToDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public Location mapFromDto(LocationDto locationDto) {
        return new Location(locationDto.getLat(), locationDto.getLon());
    }
}
