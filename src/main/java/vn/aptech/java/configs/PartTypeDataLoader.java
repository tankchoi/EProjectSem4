package vn.aptech.java.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vn.aptech.java.models.PartType;
import vn.aptech.java.repositories.PartTypeRepository;

@Component
public class PartTypeDataLoader implements CommandLineRunner {

    @Autowired
    private PartTypeRepository partTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only insert sample data if database is empty
        if (partTypeRepository.count() == 0) {
            loadSamplePartTypes();
        }
    }

    private void loadSamplePartTypes() {
        String[] partTypeNames = {
                "RAM", "SSD", "HDD", "CPU", "Mainboard",
                "VGA", "PSU", "Quạt tản nhiệt", "Ổ CD/DVD", "Card mạng",
                "Bàn phím", "Chuột", "Màn hình", "Webcam", "Loa"
        };

        for (String name : partTypeNames) {
            PartType partType = new PartType();
            partType.setName(name);
            partTypeRepository.save(partType);
        }

        System.out.println("Loaded " + partTypeNames.length + " sample PartTypes");
    }
}
