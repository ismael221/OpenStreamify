package com.ismael.movies.services;

import com.ismael.movies.model.DeviceToken;
import com.ismael.movies.repository.DeviceTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DeviceTokenService {

    final
    DeviceTokenRepository deviceTokenRepository;

    public DeviceTokenService(DeviceTokenRepository deviceTokenRepository){
        this.deviceTokenRepository = deviceTokenRepository;
    }

    public DeviceToken registerToken(DeviceToken deviceToken){
          deviceToken.setCreatedAt(Instant.now());
          return deviceTokenRepository.save(deviceToken);
    }

    public List<DeviceToken> userDeviceTokens(String userId){
       return deviceTokenRepository.findAllByUserId(userId);
    }
}
