package Tools;

import org.junit.jupiter.api.*;

class ImporterTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void upload() {
    }

    @Test
    void createJwt() {
        Importer importer = new Importer();
        importer.createJwt();
        System.out.println(importer.getAuthInfo());
    }

    @Test
    void exchangeJwtAuth() {
    }
}