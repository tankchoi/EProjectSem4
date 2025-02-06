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
    public List<Model> getAllModels() {
        return modelRepository.findAll();
    }
    public void addModel(Model model) {
        modelRepository.save(model);
    }
    public void updateModel(Model model) {
        Optional<Model> oldModel = modelRepository.findById(model.getId());
        model.setCreatedAt(oldModel.get().getCreatedAt());
        modelRepository.save(model);
    }
    public void deleteModel(long id) {
        modelRepository.deleteById(id);
    }
    public Optional<Model> findModelById(long id) {
        return modelRepository.findById(id);
    }
}
