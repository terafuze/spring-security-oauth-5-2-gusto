/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.security.oauth.samples.web;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth.samples.models.CurrentUserResponse;
import org.springframework.security.oauth.samples.models.Employee;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;


@Controller
public class GustoRestController {

  private static final Logger log = LoggerFactory.getLogger(GustoRestController.class);

  @Value("${gusto-api.current-user-endpoint}")
  private String currentUserEndpoint;

  @Value("${gusto-api.employees-endpoint}")
  private String employeesEndpoint;

  @Autowired
  private WebClient webClient;

  @GetMapping(value = "/api/gusto-token")
  public String gustoToken(
      @RegisteredOAuth2AuthorizedClient("gusto") OAuth2AuthorizedClient gustoAuthorizedClient) {

    log.debug("Gusto Access Token: {}", gustoAuthorizedClient.getAccessToken().getTokenValue());


    return gustoAuthorizedClient.getAccessToken().getTokenValue();
  }

  @GetMapping(value = "/api/current-user")
  public ResponseEntity<CurrentUserResponse> getCurrentUser(
      @RegisteredOAuth2AuthorizedClient("gusto") OAuth2AuthorizedClient gustoAuthorizedClient) {

    log.debug("Gusto Access Token: {}", gustoAuthorizedClient.getAccessToken().getTokenValue());

    ResponseEntity<CurrentUserResponse> response =
        this.webClient.get().uri(this.currentUserEndpoint).attributes(clientRegistrationId("gusto"))
            .retrieve().toEntity(CurrentUserResponse.class).block();
    log.debug("Got HTTP Response!");
    CurrentUserResponse currentUser = response.getBody();
    log.info("Current User: {}", currentUser.toString());

    return response;
  }

}
