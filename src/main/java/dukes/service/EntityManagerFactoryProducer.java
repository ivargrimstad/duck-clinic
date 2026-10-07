package dukes.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Application-scoped producer for the EntityManagerFactory.
 * With RESOURCE_LOCAL (DuckDB), Liberty cannot manage the EMF — we manage it ourselves.
 *
 * The DuckDB JDBC URL is resolved at runtime to a file inside the Liberty server output
 * directory (or /tmp as fallback). All connections to the same file share the same database,
 * which is required because DuckDB in-memory connections are isolated per-connection.
 */
@ApplicationScoped
public class EntityManagerFactoryProducer {

    private static final Logger LOG = Logger.getLogger(EntityManagerFactoryProducer.class.getName());

    private EntityManagerFactory emf;

    @PostConstruct
    public void init() {
        Map<String, Object> props = new HashMap<>();

        // Resolve a writable directory for the DuckDB file.
        // ${server.output.dir} system property is set by Liberty at startup.
        String outputDir = System.getProperty("server.output.dir");
        if (outputDir == null || outputDir.isBlank()) {
            outputDir = System.getProperty("java.io.tmpdir", "/tmp");
        }
        String dbPath = Path.of(outputDir, "duck_clinic.db").toAbsolutePath().toString();
        String jdbcUrl = "jdbc:duckdb:" + dbPath;
        LOG.info("DuckDB database path: " + dbPath);

        props.put("jakarta.persistence.jdbc.url", jdbcUrl);
        emf = Persistence.createEntityManagerFactory("duckClinicPU", props);
    }

    @PreDestroy
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}
