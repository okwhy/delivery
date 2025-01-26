package dev.delivery.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link dev.delivery.entities.TagEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto implements Serializable {
    private String tag;
}