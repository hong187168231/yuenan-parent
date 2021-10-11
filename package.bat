@echo on
@echo =============================================================
@echo $                                                           $
@echo $               LIVE Microservices-Platform                  $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  LIVE All Right Reserved                                   $
@echo $  Copyright (C) 2019-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title LIVE Microservices-Platform
@color 0e

call mvn clean package -Dmaven.test.skip=true

pause