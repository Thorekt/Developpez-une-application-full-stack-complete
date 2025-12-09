package com.thorekt.mdd.microservice.theme_service.seeder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.thorekt.mdd.microservice.theme_service.model.Theme;
import com.thorekt.mdd.microservice.theme_service.repository.ThemeRepository;

@Component
public class ThemeSeeder implements CommandLineRunner {

    private final ThemeRepository themeRepository;

    public ThemeSeeder(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (themeRepository.count() > 0) {
            return; // Database already seeded
        }

        List<Theme> themes = Arrays.asList(
                Theme.builder().title("Programmation Web")
                        .description("HTML, CSS, JavaScript, frameworks").build(),
                Theme.builder().title("Développement Backend")
                        .description("Java, Spring, .NET, Node.js").build(),
                Theme.builder().title("Développement Frontend")
                        .description("Angular, React, Vue, Svelte").build(),
                Theme.builder().title("DevOps & CI/CD")
                        .description("Docker, Kubernetes, GitLab CI, Terraform").build(),
                Theme.builder().title("Architecture Logicielle")
                        .description("Microservices, DDD, Clean Architecture").build(),
                Theme.builder().title("Développement Mobile")
                        .description("Android, iOS, Flutter, React Native").build(),
                Theme.builder().title("Cloud & Scalabilité")
                        .description("AWS, GCP, Azure, serverless").build(),
                Theme.builder().title("Sécurité Applicative")
                        .description("OWASP, Auth, Pentest App, JWT").build(),
                Theme.builder().title("Base de Données")
                        .description("SQL, NoSQL, optimisation, indexation").build(),
                Theme.builder().title("Systèmes & Bas Niveau")
                        .description("Linux, Rust, C, Kernel, OSDev").build(),
                Theme.builder().title("Intelligence Artificielle")
                        .description("LLM, MLOps, modèle IA, embeddings").build());

        themeRepository.saveAll(themes);
        Logger.getLogger(ThemeSeeder.class.getName())
                .info("Seeded " + themes.size() + " themes into the database.");
    }
}
