package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.CreateModelDTO;
import vn.aptech.java.models.Model;
import vn.aptech.java.repositories.ModelRepository;
import vn.aptech.java.services.ModelService;

import java.util.List;
import java.util.Optional;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Override
    public List<Model> getAllModels() {
        return modelRepository.findAll();
    }

    @Override
    public Optional<Model> getModelById(Long id) {
        return modelRepository.findById(id);
    }

    @Override
    public Model createModel(Model model) {
        return modelRepository.save(model);
    }

    @Override
    public Model createModel(CreateModelDTO createModelDTO) {
        Model model = new Model();
        model.setName(createModelDTO.getName());
        return modelRepository.save(model);
    }

    @Override
    public Model updateModel(Long id, Model model) {
        if (modelRepository.existsById(id)) {
            model.setId(id);
            return modelRepository.save(model);
        }
        return null;
    }

    @Override
    public Model updateModel(Long id, CreateModelDTO createModelDTO) {
        Model existingModel = modelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy model"));

        existingModel.setName(createModelDTO.getName());
        return modelRepository.save(existingModel);
    }

    @Override
    public void deleteModel(Long id) {
        modelRepository.deleteById(id);
    }

    @Override
    public List<Model> searchModels(String name) {
        return modelRepository.findByNameContainingIgnoreCase(name);
    }
}
