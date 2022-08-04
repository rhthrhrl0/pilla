package com.example.ssu_contest_eighteen_pomise.dto

data class ProtegeDTO(
    val protegesCount: Int,
    val protegesInfos: List<ProtegeInfo>
)

data class ProtegeInfo(
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