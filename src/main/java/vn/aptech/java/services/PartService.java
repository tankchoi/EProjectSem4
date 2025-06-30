package vn.aptech.java.services;

import vn.aptech.java.models.Part;

import java.util.List;
import java.util.Optional;

public interface PartService {
    List<Part> getAllParts();

    Optional<Part> getPartById(Long id);

    Part createPart(Part part);

    Part createPart(String name, Double price, Integer quantity, Long partTypeId, Integer warrantyPeriod, Long laptopId,
            String imgUrl);

    Part updatePart(Long id, Part part);

    Part updatePart(Long id, String name, Double price, Integer quantity, Long partTypeId, Integer warrantyPeriod,
            Long laptopId, String imgUrl);

    void deletePart(Long id);

    List<Part> searchParts(String name);

    List<Part> getPartsByPartType(Long partTypeId);

    List<Part> getPartsByLaptop(Long laptopId);
}
