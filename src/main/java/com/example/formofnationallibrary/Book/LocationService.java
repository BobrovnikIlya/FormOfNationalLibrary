package com.example.formofnationallibrary.Book;

import com.example.formofnationallibrary.Entities.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class LocationService {
        @Autowired
        private LocationRepository locationRepository;
        public List<Location> getAllLocation() {
            return locationRepository.findAll();
        }
    }