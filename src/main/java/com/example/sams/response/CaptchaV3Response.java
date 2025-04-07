package com.example.sams.response;

public record CaptchaV3Response (
        Boolean success,
        String challenge_ts,
        String host,
        Double score,
        String action
){
}
