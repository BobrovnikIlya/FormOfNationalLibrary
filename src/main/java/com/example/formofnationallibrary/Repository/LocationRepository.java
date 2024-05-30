package com.example.formofnationallibrary.Repository;

import com.example.formofnationallibrary.Entities.Language;
import com.example.formofnationallibrary.Entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    // Дополнительные методы, если необходимо
}
