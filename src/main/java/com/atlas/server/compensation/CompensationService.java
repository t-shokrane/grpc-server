package com.atlas.server.compensation;

import com.atlas.server.dto.RequestDto;
import org.springframework.stereotype.Component;

@Component
public class CompensationService {

    public void execute(RequestDto dto) {

        System.out.println("Compensation executed for " + dto.getCorporateId());
    }
}
