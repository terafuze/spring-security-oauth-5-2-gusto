/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth.samples.web;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth.samples.models.CurrentUserResponse;
import org.springframework.security.oauth.samples.models.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;


@Controller
public class AuthorizationController {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationController.class);
  
	@Value("${gusto-api.current-user-endpoint}")
	private String currentUserEndpoint;
	
//	@Value("${gusto-api.current-user-endpoint}")
	private String employeesEndpoint = "https://api.gusto-demo.com/v1/companies/{companyId}/employees";

	@Autowired
	private WebClient webClient;


	@GetMapping(value = "/authorize")
	public String authorizationCoddeGrant(Model model) {
	  
		log.debug("In AuthorizationController.authorizationCodeGrant() method");
		
	     CurrentUserResponse response = this.webClient.get()
	          .uri(this.currentUserEndpoint)
	          .attributes(clientRegistrationId("gusto-client-registration"))
	          .retrieve()
              .bodyToMono(CurrentUserResponse.class)
              .block();
	    log.debug("Got HTTP Response!");
	    CurrentUserResponse currentUser = response;   
	    log.info("Current User: {}", currentUser.toString());
	    
	    Long companyId = currentUser.getRoles().getPayroll_admin().getCompanies().get(0).getId();
        Employee[] employees = this.webClient.get()
            .uri(this.employeesEndpoint, companyId)
            .attributes(clientRegistrationId("gusto-client-registration"))
            .retrieve()
            .bodyToMono(Employee[].class)
            .block();
        log.debug("Got Employees!"); 
        log.info("Employee[0]: {}", employees[0].toString());
      
	    // display the index web page (see /src/main/resources/templates/index.html)
		return "index";
	}


}