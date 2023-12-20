package com.lawman.gateway.authclient.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class PageResponse<T> {
  private List<T> list;
  private Integer count;
}
