package com.example.sams.controller;

import com.example.sams.request.user.EntryRequest;
import com.example.sams.response.EntryResponse;
import com.example.sams.response.HttpResponse;
import com.example.sams.service.implementation.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticated/entry")
public class EntryController {
    private final EntryService entryService;

    @GetMapping
    public ResponseEntity<HttpResponse> findByAuthenticatedUser(Authentication authentication,Pageable pageable) {
        Page<EntryResponse> result = entryService.findByUser(authentication,pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The entries have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/date={entryDate}")
    public ResponseEntity<HttpResponse> findByAuthenticatedUserAndDate(@PathVariable("entryDate") LocalDate entryDate,Authentication authentication, Pageable pageable) {
        Page<EntryResponse> result = entryService.findByUserAndDate(authentication,entryDate,pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The entries have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<HttpResponse> save(Authentication authentication, @RequestBody @Valid EntryRequest request) {
        EntryResponse result = entryService.save(authentication, request);

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The entry has been saved successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @DeleteMapping("/id={entryId}")
    public ResponseEntity<HttpResponse> deleteById(Authentication authentication,@PathVariable("entryId") Long reviewId) {
        entryService.deleteById(authentication,reviewId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The entry has been deleted successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/id={entryId}")
    public ResponseEntity<HttpResponse> updateById(Authentication authentication,@PathVariable("entryId") Long entryId, @RequestBody @Valid EntryRequest request) {
        var result = entryService.update(authentication,request,entryId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The entry has been updated successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
