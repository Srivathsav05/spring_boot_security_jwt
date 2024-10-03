package com.ust.securerestapi.auth.controller;

public record RegisterRequest(String userName, String password, String [] roles) {
}
