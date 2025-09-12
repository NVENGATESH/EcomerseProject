package com.ecommerce.project.security.request;

import com.ecommerce.project.repositories.OtpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service

public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Transactional
    public void deleteByEmail(String email) {
        otpRepository.deleteByEmail(email);
    }
}
