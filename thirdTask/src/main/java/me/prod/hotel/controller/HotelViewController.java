package me.prod.hotel.controller;

import lombok.RequiredArgsConstructor;
import me.prod.hotel.dto.HotelCreateRequest;
import me.prod.hotel.dto.HotelResponse;
import me.prod.hotel.service.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HotelViewController {

    private final HotelService hotelService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("hotels", hotelService.getAllHotels());
        model.addAttribute("newHotel", new HotelCreateRequest());
        return "hotels";
    }

    @PostMapping("/hotels")
    public String createHotel(@ModelAttribute HotelCreateRequest request) {
        hotelService.createHotel(request);
        return "redirect:/";
    }

    @GetMapping("/hotels/{id}")
    public String showHotelDetail(@PathVariable Long id, Model model) {
        HotelResponse hotel = hotelService.getHotelById(id);
        model.addAttribute("hotel", hotel);
        return "hotel-detail";
    }
}