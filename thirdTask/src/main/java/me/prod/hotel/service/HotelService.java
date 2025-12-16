package me.prod.hotel.service;

import lombok.RequiredArgsConstructor;
import me.prod.hotel.dto.HotelCreateRequest;
import me.prod.hotel.dto.HotelResponse;
import me.prod.hotel.dto.HotelUpdateRequest;
import me.prod.hotel.entity.Hotel;
import me.prod.hotel.exception.HotelNotFoundException;
import me.prod.hotel.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    @Transactional(readOnly = true)
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Отель с ID " + id + " не найден"));
        return mapToResponse(hotel);
    }

    @Transactional
    public HotelResponse createHotel(HotelCreateRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setStars(request.getStars());

        Hotel savedHotel = hotelRepository.save(hotel);
        return mapToResponse(savedHotel);
    }

    @Transactional
    public HotelResponse updateHotel(Long id, HotelUpdateRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Отель с ID " + id + " не найден"));

        if (request.getName() != null && !request.getName().isBlank()) {
            hotel.setName(request.getName());
        }
        if (request.getDescription() != null) {
            hotel.setDescription(request.getDescription());
        }
        if (request.getStars() != null) {
            hotel.setStars(request.getStars());
        }

        Hotel updatedHotel = hotelRepository.save(hotel);
        return mapToResponse(updatedHotel);
    }

    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new HotelNotFoundException("Отель с ID " + id + " не найден");
        }
        hotelRepository.deleteById(id);
    }

    private HotelResponse mapToResponse(Hotel hotel) {
        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setDescription(hotel.getDescription());
        response.setStars(hotel.getStars());
        response.setCreatedAt(hotel.getCreatedAt());
        response.setUpdatedAt(hotel.getUpdatedAt());
        return response;
    }
}