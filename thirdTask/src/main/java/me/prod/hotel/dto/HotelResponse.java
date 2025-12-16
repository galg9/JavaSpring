package me.prod.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией об отеле")
public class HotelResponse {

    @Schema(description = "ID отеля", example = "1")
    private Long id;

    @Schema(description = "Название отеля", example = "Grand Hotel")
    private String name;

    @Schema(description = "Описание отеля", example = "Роскошный отель в центре города")
    private String description;

    @Schema(description = "Количество звезд", example = "5")
    private Integer stars;

    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления")
    private LocalDateTime updatedAt;
}