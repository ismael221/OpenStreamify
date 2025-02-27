package com.ismael.movies.controller;

import com.ismael.movies.model.DeviceToken;
import com.ismael.movies.services.DeviceTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/devices")
public class DeviceTokenController {

    final DeviceTokenService deviceTokenService;

    public DeviceTokenController(DeviceTokenService deviceTokenService){
        this.deviceTokenService = deviceTokenService;
    }

    @PostMapping("/register-token")
    public ResponseEntity<DeviceToken> registerToken(@RequestBody DeviceToken deviceToken){
            DeviceToken deviceToken1 = deviceTokenService.registerToken(deviceToken);
            return  new ResponseEntity<>(deviceToken1, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/tokens")
    public ResponseEntity<List<DeviceToken>> retrieveUserTokens(@PathVariable String userId){
        List<DeviceToken> tokens = deviceTokenService.userDeviceTokens(userId);
        return  new ResponseEntity<>(tokens,HttpStatus.OK);
    }


}
