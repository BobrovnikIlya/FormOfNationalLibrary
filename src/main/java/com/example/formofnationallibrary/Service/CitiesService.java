package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Cities;
import com.example.formofnationallibrary.Repository.CitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitiesService {

    @Autowired
    private CitiesRepository cityRepository;

    public List<Cities> findCitiesByNameContaining(String term) {
        return cityRepository.findByNameContaining(term);
    }

    public List<Cities> getAllCities() {
        return cityRepository.findAll();
    }
    public Cities findCitiesByName(String name) {
        return cityRepository.findByName(name);
    }

    public void saveCities(Cities city) {
        cityRepository.save(city);
    }

    // Другие методы, если необходимо
}