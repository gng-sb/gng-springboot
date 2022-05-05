package com.gng.springboot.commons.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:/application-test.yml")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class BaseServiceTest {

}
