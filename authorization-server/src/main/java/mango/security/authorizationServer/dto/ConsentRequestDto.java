package mango.security.authorizationServer.dto;

public record ConsentRequestDto(
        String client_id,
        String scope,
        String redirect_uri
) {}
