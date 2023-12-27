package org.freedomfinancestack.razorpay.cas.acs.service.parser;

import org.freedomfinancestack.razorpay.cas.acs.service.parser.impl.AppChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.impl.BrowserChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeRequestParserFactoryTest {

    @Mock private BrowserChallengeRequestParser browserChallengeRequestParser;

    @Mock private AppChallengeRequestParser appChallengeRequestParser;

    private ChallengeRequestParserFactory parserFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        parserFactory =
                new ChallengeRequestParserFactory(
                        browserChallengeRequestParser, appChallengeRequestParser);
    }

    @Test
    void getService_WithAppChannel_ShouldReturnAppParser() {
        // Arrange
        DeviceChannel appChannel = DeviceChannel.APP;

        // Act
        ChallengeRequestParser parser = parserFactory.getService(appChannel);

        // Assert
        assertEquals(
                appChallengeRequestParser,
                parser,
                "getService should return AppChallengeRequestParser for APP channel");
    }

    @Test
    void getService_WithBrowserChannel_ShouldReturnBrowserParser() {
        // Arrange
        DeviceChannel browserChannel = DeviceChannel.BRW;

        // Act
        ChallengeRequestParser parser = parserFactory.getService(browserChannel);

        // Assert
        assertEquals(
                browserChallengeRequestParser,
                parser,
                "getService should return BrowserChallengeRequestParser for BRW channel");
    }

    @Test
    void getService_WithNullChannel_ShouldReturnNull() {
        // Arrange
        DeviceChannel nullChannel = null;

        // Act
        ChallengeRequestParser parser = parserFactory.getService(nullChannel);

        // Assert
        assertNull(parser, "getService should return null for null channel");
    }
}
