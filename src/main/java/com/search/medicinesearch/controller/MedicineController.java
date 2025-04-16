package com.search.medicinesearch.controller;

import com.search.medicinesearch.model.Medicine;
import com.search.medicinesearch.service.MedicineSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@CrossOrigin
public class MedicineController {

    private final MedicineSearchService medicineSearchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query) {
        List<Medicine> result = medicineSearchService.autocomplete(query);
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }
}
