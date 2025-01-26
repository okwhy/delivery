package dev.delivery.dtos.auth;

import lombok.Data;

import java.io.Serializable;

public record JwtResponce(String token) implements Serializable {
}
