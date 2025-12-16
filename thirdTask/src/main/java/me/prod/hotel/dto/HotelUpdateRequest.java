package me.prod.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление информации об отеле")
public class HotelUpdateRequest {

    @Schema(description = "Название отеля", example = "Grand Hotel Updated")
    private String name;

    @Schema(description = "Описание отеля", example = "Обновленное описание отеля")
    private String description;

    @Min(value = 1, message = "Количество звезд должно быть от 1 до 5")
    @Max(value = 5, message = "Количество звезд должно быть от 1 до 5")
    @Schema(description = "Количество звезд (от 1 до 5)", example = "4")
    private Integer stars;
}