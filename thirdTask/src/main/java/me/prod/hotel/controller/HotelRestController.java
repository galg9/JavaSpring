package me.prod.hotel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.prod.hotel.dto.HotelCreateRequest;
import me.prod.hotel.dto.HotelResponse;
import me.prod.hotel.dto.HotelUpdateRequest;
import me.prod.hotel.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Hotel API", description = "API для управления отелями")
public class HotelRestController {

    private final HotelService hotelService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить список всех отелей", description = "Возвращает список всех отелей в системе")
    @ApiResponse(responseCode = "200", description = "Список отелей успешно получен")
    public List<HotelResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить отель по ID", description = "Возвращает информацию об отеле по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отель найден"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public HotelResponse getHotelById(
            @Parameter(description = "ID отеля") @PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новый отель", description = "Создает новый отель в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отель успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public HotelResponse createHotel(
            @Parameter(description = "Данные для создания отеля") @Valid @RequestBody HotelCreateRequest request) {
        return hotelService.createHotel(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновить информацию об отеле", description = "Обновляет информацию об отеле по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отель успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Отель не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public HotelResponse updateHotel(
            @Parameter(description = "ID отеля") @PathVariable Long id,
            @Parameter(description = "Данные для обновления отеля") @Valid @RequestBody HotelUpdateRequest request) {
        return hotelService.updateHotel(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить отель", description = "Удаляет отель из системы по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Отель успешно удален"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public void deleteHotel(
            @Parameter(description = "ID отеля") @PathVariable Long id) {
        hotelService.deleteHotel(id);
    }
}