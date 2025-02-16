package ait.hwds;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "mail.username=fake",
        "mail.password=fake"
})
class HwdsApplicationTests {

    @Test
    void contextLoads() {
    }

}
