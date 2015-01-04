/*
 * Copyright 2014 the original author or authors.
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
package com.sky.server.config.social.user;

import com.sky.commons.domain.User;
import com.sky.server.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public final class SimpleConnectionSignUp implements ConnectionSignUp {
	private static Logger logger = LoggerFactory.getLogger(SimpleConnectionSignUp.class);

  @Autowired
  private UserRepository userRepository;

	public String execute(Connection<?> connection) {
		GitHub github = (GitHub)connection.getApi();
		GitHubUserProfile userProfile = github.userOperations().getUserProfile();

		User user = userRepository.findByEmail(userProfile.getEmail());
		logger.debug("findByEmail: {}", user);
		if (user == null)
			user = this.create(userProfile);

		return Long.toString(user.getId());
	}

	@Transactional
	private User create(GitHubUserProfile userProfile) {
		User user = new User();
		user.setEmail(userProfile.getEmail());
		user.setName(userProfile.getName());
		user.setImageUrl(userProfile.getAvatarUrl());

		return userRepository.save(user);
	}

}
