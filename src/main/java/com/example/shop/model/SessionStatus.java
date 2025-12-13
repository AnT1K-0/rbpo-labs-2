package com.example.shop.model;

public enum SessionStatus {
    ACTIVE,     // можно использовать refresh-token
    REFRESHED,  // по этой сессии уже выдали новую пару токенов
    REVOKED,    // вручную отозвана (logout и т.п.)
    EXPIRED     // срок действия истёк
}
