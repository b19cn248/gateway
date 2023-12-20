package com.lawman.gateway.authserver.service;


public interface RedisService {
  void set(String key, Object value);
  void hashSet(String key, String field, Object value);
}
