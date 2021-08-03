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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

/**
 * @author Joe Grandja
 */
@Controller
public class AuthorizationController {

	@Value("${gusto-api.base-uri}")
	private String gustoApiURL;

	@Autowired
	private WebClient webClient;


	@GetMapping(value = "/authorize", params = "grant_type=authorization_code")
	public String authorization_code_grant(Model model) {
	  
		System.out.println("Wrong function");
		
	      ResponseSpec response = this.webClient
	          .get()
	          .uri(this.gustoApiURL)
	          .attributes(clientRegistrationId("gusto-client"))
	          .retrieve();
		
	    System.out.print("Got HTTP Response!");
	    
	    System.out.println("Response.toString: " + response.toString());
	    
//		model.addAttribute("messages", messages);
		
		return "index";
	}

	@GetMapping("/authorized")		// registered redirect_uri for authorization_code
	public String authorized(Model model) {
		
	  System.out.println("redirect uri in authorized function");

	  //		String[] messages = retrieveMessages("messaging-client-auth-code");
	  //		model.addAttribute("messages", messages);

	  return "login";
	}

}