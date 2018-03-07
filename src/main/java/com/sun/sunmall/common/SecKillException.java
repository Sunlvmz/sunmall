package com.sun.sunmall.common;


public class SecKillException extends RuntimeException {

    private SecKillEnum secKillEnum;

    public SecKillException(SecKillEnum secKillEnum){
        this.secKillEnum = secKillEnum;
    }
}
