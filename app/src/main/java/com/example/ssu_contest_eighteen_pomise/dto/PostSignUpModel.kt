package com.example.ssu_contest_eighteen_pomise.dto

data class PostSignUpModel(
    var email: String,
    var isGuardian:Boolean,
    var password: String,
    var phoneNumber: String,
    var role: String,
    var username: String
)

