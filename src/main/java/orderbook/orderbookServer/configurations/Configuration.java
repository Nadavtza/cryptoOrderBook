package orderbook.orderbookServer.configurations;

import orderbook.orderbookServer.services.ExchangeManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public ExchangeManagerService getExchangeManagerService() {
        return new ExchangeManagerService();
    }
}
