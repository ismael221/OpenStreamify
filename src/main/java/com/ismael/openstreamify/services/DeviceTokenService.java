package com.ismael.openstreamify.services;

import com.ismael.openstreamify.model.DeviceToken;
import com.ismael.openstreamify.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceTokenService.class);

    private final DeviceTokenRepository deviceTokenRepository;

    public DeviceToken registerToken(DeviceToken deviceToken) {
        deviceToken.setCreatedAt(Instant.now());
        logger.info("Registering device token: " + deviceToken);
        return deviceTokenRepository.save(deviceToken);
    }

    public List<DeviceToken> userDeviceTokens(String userId) {
        logger.info("Getting device tokens for: " + userId);
        return deviceTokenRepository.findAllByUserId(userId);
    }
}
