package com.street.common.config;

import java.time.Duration;

// import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import com.street.common.utils.Receiver;


@Configuration
public class RedisConfig {

    // @Value("${spring.redis.host}")
    private String redisHost= "127.0.0.1";

    // @Value("${spring.redis.port}")
    private int redisPort=6379;

    // @Value("${spring.redis.pass}")
    private String redisPass="Aj189628@";

    // @Value("${spring.redis.db}")
    private int redisDb=5;


    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);


    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapter) {

      RedisMessageListenerContainer container = new RedisMessageListenerContainer();
      container.setConnectionFactory(connectionFactory);
      container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

      return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
      return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    Receiver receiver() {
      return new Receiver();
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
      return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
      RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

      if(redisPass != null) {
          configuration.setPassword(String.valueOf(redisPass));
      }

      if(!String.valueOf(redisDb).trim().isEmpty()) {
          configuration.setDatabase(redisDb);
      }

      return new LettuceConnectionFactory(configuration);
    }
    
  //  @Bean
  //  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
  //    return RedisCacheManager.create(connectionFactory);
  //  }

    @Bean
    public RedisCacheManager cacheManager() {
      RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

      return RedisCacheManager.builder(redisConnectionFactory())
          .cacheDefaults(cacheConfig)
          .withCacheConfiguration("tutorials", myDefaultCacheConfig(Duration.ofMinutes(5)))
          .withCacheConfiguration("tutorial", myDefaultCacheConfig(Duration.ofMinutes(1)))
          .build();
    }

    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
      return RedisCacheConfiguration
          .defaultCacheConfig()
          .entryTtl(duration)
          .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
