package dev.delivery.services;

import dev.delivery.entities.*;
import dev.delivery.enums.Availability;
import dev.delivery.enums.Role;
import dev.delivery.repos.CredentialRepo;
import dev.delivery.repos.PerformerRepo;
import dev.delivery.repos.ProductRepo;
import dev.delivery.repos.TagRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Getter
public class InitializerService {
    private final PerformerRepo performerRepo;
    private final ProductRepo productRepo;
    private final TagRepo tagRepo;
    private final CredentialRepo credentialRepo;


    private final Faker faker = new Faker();

    public void init() {
        initPerformers();
        initTags();
        initProducts();
    }

    private void initPerformers() {
        for (int i = 0; i < 10; i++) {
            PerformerEntity performer = new PerformerEntity();
            performer.setFio(faker.name().fullName());
            performer.setPhone(faker.phoneNumber().phoneNumber());
            CredentialEntity credential = new CredentialEntity();
            switch (i) {
                case 0:
                    performer.setRole(Role.valueOf("MANAGER"));
                    credential.setUsername("aaaa@gmail.com");
                    break;
                case 1:
                    performer.setRole(Role.valueOf("PACKER"));
                    credential.setUsername("bbbb@gmail.com");
                    break;
                case 2:
                    performer.setRole(Role.valueOf("COURIER"));
                    credential.setUsername("cccc@gmail.com");
                    break;
                default:
                    performer.setRole(Role.valueOf(faker.options().option("MANAGER", "COURIER", "PACKER")));
                    credential.setUsername(faker.internet().emailAddress());
            }

            credential.setPassword(new PasswordEntity("1234"));
            credential = credentialRepo.save(credential);
            performer.setCredential(credential);
            performerRepo.save(performer);
        }
    }

    private void initTags() {
        for (int i = 0; i < 10; i++) {
            TagEntity tag = new TagEntity();
            tag.setTag(faker.lorem().word());
            tagRepo.save(tag);
        }
    }

    private void initProducts() {
        for (int i = 0; i < 25; i++) {
            ProductEntity product = new ProductEntity();
            product.setTitle(faker.commerce().productName());
            product.setDescription(faker.lorem().sentence());
            if (i == 0) {
                product.setAvailability(Availability.valueOf("IN_STOCK"));
            } else {
                product.setAvailability(Availability.valueOf(faker.options().option("IN_STOCK", "OUT_OF_STOCK", "NOT_AVAILABLE")));
            }
            product.setPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)));

            List<ProductTagEntity> productTags = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ProductTagEntity productTag = new ProductTagEntity();
                productTag.setProduct(product);
                productTag.setTag(tagRepo.findAll().get((int) faker.number().numberBetween(0, tagRepo.count() - 1)));
                productTags.add(productTag);
            }
            product.setProductTags(productTags);
            productRepo.save(product);
        }
    }
}
