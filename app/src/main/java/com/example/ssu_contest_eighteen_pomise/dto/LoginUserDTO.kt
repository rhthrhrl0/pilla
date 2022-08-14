package com.example.ssu_contest_eighteen_pomise.dto

data class LoginUserDTO(
    var email: String,
    var username: String,
    var role: String,
    var accessToken: String,
    var refreshToken: String,
    var isGuardian:Boolean,
    var phoneNumber:String
)

