package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Model;
import vn.aptech.java.repositories.ModelRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ModelService {
    @Autowired
    private ModelRepository modelRepository;

}
