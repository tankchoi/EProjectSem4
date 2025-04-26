package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.PartType;
import vn.aptech.java.repositories.PartTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PartTypeService {
    @Autowired
    private PartTypeRepository partTypeRepository;

}
