package mango.security.authorizationServer.dto;

public record ConsentConfirmRequestDto(
        String client_id,
        String principal_name,
        String scope // "read write"
) {}
