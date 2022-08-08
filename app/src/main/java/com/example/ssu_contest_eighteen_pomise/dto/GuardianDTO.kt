package com.example.ssu_contest_eighteen_pomise.dto

data class GuardianDTO(
    val guardianInfos: List<GuardianInfo>,
    val guardiansCount: Int
)

data class GuardianInfo(
    val createdAt: String,
    val email: String,
    val guardians: List<Guardian>,
    val id: Int,
    val images: List<Image>,
    val isGuardian: Boolean,
    val password: String,
    val phoneNumber: String,
    val pill: List<Pill>,
    val proteges: List<Protege>,
    val pushToken: PushToken,
    val refreshToken: String,
    val role: String,
    val updatedDate: String,
    val username: String
)

data class Guardian(
    val createdAt: String,
    val id: Int,
    val updatedDate: String
)

data class Protege(
    val createdAt: String,
    val id: Int,
    val updatedDate: String
)

data class PushToken(
    val createdAt: String,
    val id: Int,
    val token: String,
    val updatedDate: String
)

data class Image(
    val createdAt: String,
    val id: Int,
    val imageUrl: String,
    val updatedDate: String
)

data class Pill(
    val createdAt: String,
    val id: Int,
    val pillCategory: String,
    val pillName: String,
    val time: String,
    val updatedDate: String
)