package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.dtos.admin.CreateModelDTO;
import vn.aptech.java.dtos.admin.UpdateModelDTO;
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
    public List<Model> getModels(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return modelRepository.findAll();
        } else {
            return modelRepository.findByNameContainingIgnoreCase(keyword);
        }
    }

    @Override
    public Optional<Model> getModelById(Long id) {
        return modelRepository.findById(id);
    }

    @Override
    public void createModel(CreateModelDTO createModelDTO) {
        Model model = new Model();
        model.setName(createModelDTO.getName());
        modelRepository.save(model);
    }

    @Override
    public void updateModel(UpdateModelDTO updateModelDTO) {
        Optional<Model> modelOpt = modelRepository.findById(updateModelDTO.getId());
        if (modelOpt.isPresent()) {
            Model model = modelOpt.get();
            model.setName(updateModelDTO.getName());
            modelRepository.save(model);
        } else {
            throw new IllegalArgumentException("Model with ID " + updateModelDTO.getId() + " does not exist.");
        }
    }

    @Override
    public void deleteModel(Long id) {
        modelRepository.deleteById(id);
    }
}
