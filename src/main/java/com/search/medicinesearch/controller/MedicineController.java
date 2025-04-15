package com.search.medicinesearch.controller;

import com.search.medicinesearch.model.Medicine;
import com.search.medicinesearch.service.MedicineSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.io.IOException;



import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@CrossOrigin
public class MedicineController {

    private final MedicineSearchService medicineSearchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query) {
        try {
            List<Medicine> results = medicineSearchService.autocomplete(query);
            return ResponseEntity.ok(results);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while searching medicines: " + e.getMessage());
        }
    }

}
