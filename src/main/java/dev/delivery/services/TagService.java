package dev.delivery.services;

import dev.delivery.dtos.TagDto;
import dev.delivery.mappers.TagMapper;
import dev.delivery.repos.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepo tagRepo;
    private final TagMapper tagMapper;

    public List<TagDto> getAllTags() {
        return tagRepo.findAll().stream().map(tagMapper::toDto).toList();
    }
}
