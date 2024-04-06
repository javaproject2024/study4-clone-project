package com.example.securityhibernate.service.imp;

public interface UsersServiceImp {

    boolean addUser(String username, String password, String role);
    boolean checkLogin(String username, String password);

}
