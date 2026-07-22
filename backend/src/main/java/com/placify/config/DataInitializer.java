package com.placify.config;

import com.placify.model.JobRole;
import com.placify.model.Skill;
import com.placify.model.User;
import com.placify.model.enums.UserRole;
import com.placify.repository.JobRoleRepository;
import com.placify.repository.SkillRepository;
import com.placify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class DataInitializer {

    @Value("${placify.demo.admin.email:admin@placify.com}")
    private String adminEmail;

    @Value("${placify.demo.admin.password:admin123}")
    private String adminPassword;

    @Value("${placify.demo.student.email:student@placify.com}")
    private String studentEmail;

    @Value("${placify.demo.student.password:student123}")
    private String studentPassword;

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               SkillRepository skillRepository,
                               JobRoleRepository jobRoleRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = User.builder()
                        .name("Admin")
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(admin);
            }

            // Create demo student if not exists
            if (!userRepository.existsByEmail(studentEmail)) {
                User student = User.builder()
                        .name("Demo Student")
                        .email(studentEmail)
                        .password(passwordEncoder.encode(studentPassword))
                        .role(UserRole.STUDENT)
                        .build();
                userRepository.save(student);
            }

            // Initialize skills if empty
            if (skillRepository.count() == 0) {
                List<Skill> skills = new ArrayList<>();

                // Programming Languages
                skills.add(Skill.builder().name("Java").category("Programming Language")
                        .learningResource("https://docs.oracle.com/javase/tutorial/")
                        .learningDescription("Master Java fundamentals: OOP, collections, streams, and multithreading").build());
                skills.add(Skill.builder().name("Python").category("Programming Language")
                        .learningResource("https://docs.python.org/3/tutorial/")
                        .learningDescription("Learn Python syntax, data structures, and scripting").build());
                skills.add(Skill.builder().name("JavaScript").category("Programming Language")
                        .learningResource("https://javascript.info/")
                        .learningDescription("Study modern JavaScript: ES6+, async/await, DOM manipulation").build());
                skills.add(Skill.builder().name("C++").category("Programming Language")
                        .learningResource("https://cplusplus.com/doc/tutorial/")
                        .learningDescription("Learn C++ fundamentals: pointers, memory management, STL").build());
                skills.add(Skill.builder().name("TypeScript").category("Programming Language")
                        .learningResource("https://www.typescriptlang.org/docs/")
                        .learningDescription("Learn TypeScript type system, interfaces, and generics").build());

                // Frameworks
                skills.add(Skill.builder().name("Spring Boot").category("Framework")
                        .learningResource("https://spring.io/guides")
                        .learningDescription("Build REST APIs with Spring Boot: auto-configuration, starters, and dependency injection").build());
                skills.add(Skill.builder().name("Spring MVC").category("Framework")
                        .learningResource("https://spring.io/guides/gs/serving-web-content/")
                        .learningDescription("Learn Spring MVC request mapping, controllers, and view resolution").build());
                skills.add(Skill.builder().name("Hibernate").category("Framework")
                        .learningResource("https://hibernate.org/orm/documentation/")
                        .learningDescription("Master Hibernate ORM: entity mapping, HQL, caching, and transactions").build());
                skills.add(Skill.builder().name("React").category("Framework")
                        .learningResource("https://react.dev/learn")
                        .learningDescription("Learn React components, hooks, state management, and JSX").build());
                skills.add(Skill.builder().name("Angular").category("Framework")
                        .learningResource("https://angular.io/tutorial")
                        .learningDescription("Study Angular: components, services, modules, and RxJS").build());
                skills.add(Skill.builder().name("Node.js").category("Framework")
                        .learningResource("https://nodejs.org/en/docs/guides")
                        .learningDescription("Learn server-side JavaScript with Node.js and Express").build());

                // Databases
                skills.add(Skill.builder().name("SQL").category("Database")
                        .learningResource("https://www.w3schools.com/sql/")
                        .learningDescription("Master SQL queries: joins, subqueries, aggregations, and indexing").build());
                skills.add(Skill.builder().name("PostgreSQL").category("Database")
                        .learningResource("https://www.postgresql.org/docs/current/tutorial.html")
                        .learningDescription("Learn PostgreSQL: schemas, functions, triggers, and performance tuning").build());
                skills.add(Skill.builder().name("MySQL").category("Database")
                        .learningResource("https://dev.mysql.com/doc/refman/8.0/en/tutorial.html")
                        .learningDescription("Study MySQL database design, queries, and administration").build());
                skills.add(Skill.builder().name("MongoDB").category("Database")
                        .learningResource("https://www.mongodb.com/docs/manual/tutorial/")
                        .learningDescription("Learn MongoDB document modeling, CRUD, and aggregation pipeline").build());

                // Web Technologies
                skills.add(Skill.builder().name("HTML").category("Web Technology")
                        .learningResource("https://developer.mozilla.org/en-US/docs/Learn/HTML")
                        .learningDescription("Learn semantic HTML5: forms, accessibility, and best practices").build());
                skills.add(Skill.builder().name("CSS").category("Web Technology")
                        .learningResource("https://developer.mozilla.org/en-US/docs/Learn/CSS")
                        .learningDescription("Master CSS: Flexbox, Grid, animations, and responsive design").build());
                skills.add(Skill.builder().name("REST API").category("Web Technology")
                        .learningResource("https://restfulapi.net/")
                        .learningDescription("Understand RESTful API design: HTTP methods, status codes, and HATEOAS").build());

                // Tools & DevOps
                skills.add(Skill.builder().name("Git").category("Tool")
                        .learningResource("https://git-scm.com/doc")
                        .learningDescription("Practice Git workflows: branching, merging, rebasing, and collaboration").build());
                skills.add(Skill.builder().name("Maven").category("Tool")
                        .learningResource("https://maven.apache.org/guides/")
                        .learningDescription("Learn Maven build lifecycle, dependency management, and plugins").build());
                skills.add(Skill.builder().name("Docker").category("Tool")
                        .learningResource("https://docs.docker.com/get-started/")
                        .learningDescription("Learn containerization: Dockerfiles, images, volumes, and compose").build());

                // Computer Science Fundamentals
                skills.add(Skill.builder().name("OOP").category("Concept")
                        .learningResource("https://www.geeksforgeeks.org/object-oriented-programming-oops-concept-in-java/")
                        .learningDescription("Master OOP pillars: encapsulation, inheritance, polymorphism, abstraction").build());
                skills.add(Skill.builder().name("DSA").category("Concept")
                        .learningResource("https://www.geeksforgeeks.org/data-structures/")
                        .learningDescription("Practice data structures and algorithms: arrays, trees, graphs, DP, sorting").build());
                skills.add(Skill.builder().name("Design Patterns").category("Concept")
                        .learningResource("https://refactoring.guru/design-patterns")
                        .learningDescription("Learn common design patterns: Singleton, Factory, Observer, Strategy").build());
                skills.add(Skill.builder().name("System Design").category("Concept")
                        .learningResource("https://github.com/donnemartin/system-design-primer")
                        .learningDescription("Study system design: scalability, load balancing, caching, microservices").build());

                // Testing
                skills.add(Skill.builder().name("JUnit").category("Testing")
                        .learningResource("https://junit.org/junit5/docs/current/user-guide/")
                        .learningDescription("Learn unit testing with JUnit 5: assertions, lifecycle, and parameterized tests").build());
                skills.add(Skill.builder().name("Selenium").category("Testing")
                        .learningResource("https://www.selenium.dev/documentation/")
                        .learningDescription("Practice browser automation and E2E testing with Selenium WebDriver").build());

                // Cloud
                skills.add(Skill.builder().name("AWS").category("Cloud")
                        .learningResource("https://aws.amazon.com/getting-started/")
                        .learningDescription("Learn AWS core services: EC2, S3, RDS, Lambda, and IAM").build());
                skills.add(Skill.builder().name("Spring Security").category("Framework")
                        .learningResource("https://spring.io/guides/gs/securing-web/")
                        .learningDescription("Implement authentication and authorization with Spring Security").build());

                skillRepository.saveAll(skills);
            }

            // Initialize job roles if empty
            if (jobRoleRepository.count() == 0) {
                // Java Backend Developer
                Set<Skill> javaBackendSkills = new HashSet<>();
                Arrays.asList("Java", "Spring Boot", "Spring MVC", "Hibernate", "SQL", "PostgreSQL",
                                "REST API", "Git", "Maven", "OOP", "Design Patterns", "JUnit", "Spring Security")
                        .forEach(name -> skillRepository.findByNameIgnoreCase(name).ifPresent(javaBackendSkills::add));

                JobRole javaBackend = JobRole.builder()
                        .roleName("Java Backend Developer")
                        .description("Develops server-side applications using Java and Spring ecosystem. Responsible for building REST APIs, database integration, and business logic implementation.")
                        .requiredSkills(javaBackendSkills)
                        .minMatchThreshold(60)
                        .build();
                jobRoleRepository.save(javaBackend);

                // Full Stack Java Developer
                Set<Skill> fullStackSkills = new HashSet<>();
                Arrays.asList("Java", "Spring Boot", "Hibernate", "SQL", "PostgreSQL", "REST API",
                                "HTML", "CSS", "JavaScript", "React", "Git", "Maven", "OOP")
                        .forEach(name -> skillRepository.findByNameIgnoreCase(name).ifPresent(fullStackSkills::add));

                JobRole fullStack = JobRole.builder()
                        .roleName("Full Stack Java Developer")
                        .description("Builds end-to-end web applications combining Java backend with modern frontend frameworks. Handles both server-side logic and client-side UI development.")
                        .requiredSkills(fullStackSkills)
                        .minMatchThreshold(55)
                        .build();
                jobRoleRepository.save(fullStack);

                // Software Engineer
                Set<Skill> seSkills = new HashSet<>();
                Arrays.asList("Java", "Python", "SQL", "Git", "OOP", "DSA", "System Design",
                                "REST API", "Design Patterns")
                        .forEach(name -> skillRepository.findByNameIgnoreCase(name).ifPresent(seSkills::add));

                JobRole se = JobRole.builder()
                        .roleName("Software Engineer")
                        .description("General software engineering role requiring strong fundamentals in programming, algorithms, and system design. Expected to work across the full development lifecycle.")
                        .requiredSkills(seSkills)
                        .minMatchThreshold(50)
                        .build();
                jobRoleRepository.save(se);

                // QA Engineer
                Set<Skill> qaSkills = new HashSet<>();
                Arrays.asList("Java", "Selenium", "JUnit", "SQL", "Git", "REST API", "Python")
                        .forEach(name -> skillRepository.findByNameIgnoreCase(name).ifPresent(qaSkills::add));

                JobRole qa = JobRole.builder()
                        .roleName("QA Engineer")
                        .description("Ensures software quality through manual and automated testing. Develops test strategies, writes test cases, and performs regression testing.")
                        .requiredSkills(qaSkills)
                        .minMatchThreshold(50)
                        .build();
                jobRoleRepository.save(qa);

                // Frontend Developer
                Set<Skill> frontendSkills = new HashSet<>();
                Arrays.asList("HTML", "CSS", "JavaScript", "TypeScript", "React", "Angular", "Git", "REST API")
                        .forEach(name -> skillRepository.findByNameIgnoreCase(name).ifPresent(frontendSkills::add));

                JobRole frontend = JobRole.builder()
                        .roleName("Frontend Developer")
                        .description("Creates responsive, interactive user interfaces. Expertise in modern JavaScript frameworks and UI/UX best practices.")
                        .requiredSkills(frontendSkills)
                        .minMatchThreshold(55)
                        .build();
                jobRoleRepository.save(frontend);

                // DevOps Engineer
                Set<Skill> devopsSkills = new HashSet<>();
                Arrays.asList("Git", "Docker", "AWS", "Python", "SQL", "Maven")
                        .forEach(name -> skillRepository.findByNameIgnoreCase(name).ifPresent(devopsSkills::add));

                JobRole devops = JobRole.builder()
                        .roleName("DevOps Engineer")
                        .description("Manages CI/CD pipelines, cloud infrastructure, and deployment automation. Bridges development and operations teams.")
                        .requiredSkills(devopsSkills)
                        .minMatchThreshold(50)
                        .build();
                jobRoleRepository.save(devops);
            }
        };
    }
}
