package com.example.formofnationallibrary.Service;

import com.example.formofnationallibrary.Entities.Location;
import com.example.formofnationallibrary.Repository.LocationRepository;
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