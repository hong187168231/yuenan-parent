@echo on
@echo =============================================================
@echo $                                                           $
@echo $               INDO Microservices-Platform                  $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  INDO All Right Reserved                                   $
@echo $  Copyright (C) 2019-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title INDO Microservices-Platform
@color 0e

call mvn clean package -Dmaven.test.skip=true

pause