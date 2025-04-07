package com.example.sams.service.implementation;

import com.example.sams.response.CaptchaV2Response;
import com.example.sams.response.CaptchaV3Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CaptchaService {
    private final RestTemplate restTemplate;

    @Value("${recaptcha.secret-key}")
    private String secretKey;

    @Value("${recaptcha.verify-url}")
    private String verifyUrl;

    public void validateTokenV2(String token) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map= new LinkedMultiValueMap<>();
        map.add("secret", secretKey);
        map.add("response", token);

        HttpEntity<MultiValueMap<String,String>> entity=new HttpEntity<>(map,headers);

        ResponseEntity<CaptchaV2Response> response = restTemplate.exchange(
                verifyUrl,
                HttpMethod.POST,
                entity,
                CaptchaV2Response.class
        );

        CaptchaV2Response captchaResponse = response.getBody();

        if(captchaResponse==null || !captchaResponse.success()){
            throw new SecurityException("The provided recaptcha is invalid.");
        }
    }

    public void validateTokenV3(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map= new LinkedMultiValueMap<>();
        map.add("secret", secretKey);
        map.add("response", token);

        HttpEntity<MultiValueMap<String,String>> entity=new HttpEntity<>(map,headers);
        ResponseEntity<CaptchaV3Response> response = restTemplate.exchange(
                verifyUrl,
                HttpMethod.POST,
                entity,
                CaptchaV3Response.class
        );
        CaptchaV3Response captchaResponse = response.getBody();

        if(captchaResponse==null || !captchaResponse.success()){
            throw new SecurityException("The provided recaptcha is invalid.");
        }
    }
}
