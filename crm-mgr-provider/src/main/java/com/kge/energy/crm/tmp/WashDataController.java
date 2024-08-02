package com.kge.energy.crm.tmp;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjihua
 */
@RestController
@RequestMapping("/washData")
@RequiredArgsConstructor
public class WashDataController {

    private final WashDataService washDataService;

    @GetMapping("/insertResource")
    public void insertResource() {
        washDataService.insertResource();
    }
}
