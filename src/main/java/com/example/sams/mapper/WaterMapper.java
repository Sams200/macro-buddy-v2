package com.example.sams.mapper;

import com.example.sams.entity.Water;
import com.example.sams.response.WaterResponse;
import org.springframework.stereotype.Service;

@Service
public class WaterMapper {
    public WaterResponse toResponse(Water water) {
        return new WaterResponse(
                water.getWaterId(),
                water.getDate(),
                water.getAmount()
        );
    }
}
