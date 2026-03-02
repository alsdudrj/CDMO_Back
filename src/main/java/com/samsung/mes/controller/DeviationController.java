package com.samsung.mes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsung.mes.dto.DeviationDTO;
import com.samsung.mes.service.DeviationService;
//import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/deviations")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DeviationController {

    private final DeviationService deviationService;

    @Autowired
    public DeviationController(DeviationService deviationService) {
        this.deviationService = deviationService;
    }

    @GetMapping("/simulate")
    public DeviationDTO simulateDeviation() {
        return deviationService.simulateDeviation();
    }
}
