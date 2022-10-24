package com.indo.user.service;

import javax.servlet.http.HttpServletRequest;

public interface TestService {


    String sayHello(String name, HttpServletRequest request);

    String circuitBreaker(String name, HttpServletRequest request);
}
