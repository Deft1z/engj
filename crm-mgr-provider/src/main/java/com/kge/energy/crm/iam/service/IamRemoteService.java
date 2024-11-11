package com.kge.energy.crm.iam.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

@HttpExchange
public interface IamRemoteService {

    @GetExchange("/api/v3/user/paged")
    Map<String, Object> getUserByPage(@RequestParam("filter") String filter, @RequestParam("pageSize") String pageSize, @RequestParam(value = "cookie", required = false) String cookie, @RequestParam("timeZone") String timeZone);

}