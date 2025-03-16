package com.example.sams.mapper;

import com.example.sams.entity.Entry;
import com.example.sams.response.EntryResponse;
import org.springframework.stereotype.Service;

@Service
public class EntryMapper {
    public EntryResponse toEntryResponse(Entry entry){
        return new EntryResponse(
                entry.getEntryId(),
                entry.getDate(),
                entry.getMeal().getMeal(),
                entry.getQuantity(),
                entry.getFood()

        );
    }
}
