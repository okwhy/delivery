package dev.delivery.services;

import dev.delivery.dtos.ClientDto;
import dev.delivery.entities.ClientEntity;
import dev.delivery.mappers.ClientMapper;
import dev.delivery.repos.ClientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepo clientRepo;
    private final ClientMapper clientMapper;

    public ClientDto login(String phoneNumber) {

        ClientEntity client = clientRepo.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new IllegalArgumentException("Client with phone number " + phoneNumber + " doesnt exists."));
        return clientMapper.toDto(client);
    }

    public ClientDto create(ClientDto client) {

        if (clientRepo.existsByPhoneNumber(client.getPhoneNumber())) {
            throw new IllegalArgumentException("Client with phone number " + client.getPhoneNumber() + " already exists.");
        }
        return clientMapper.toDto(clientRepo.save(clientMapper.toEntity(client)));
    }
}
