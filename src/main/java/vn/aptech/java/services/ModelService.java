package vn.aptech.java.services;

import vn.aptech.java.dtos.CreateModelDTO;
import vn.aptech.java.models.Model;
import java.util.List;
import java.util.Optional;

public interface ModelService {
    List<Model> getAllModels();

    Optional<Model> getModelById(Long id);

    Model createModel(Model model);

    Model createModel(CreateModelDTO createModelDTO);

    Model updateModel(Long id, Model model);

    Model updateModel(Long id, CreateModelDTO createModelDTO);

    void deleteModel(Long id);

    List<Model> searchModels(String name);
}
