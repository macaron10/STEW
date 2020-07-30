package com.ssafy.study.common.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userEmail){
        super(userEmail + " NotFoundException");
    }

}
