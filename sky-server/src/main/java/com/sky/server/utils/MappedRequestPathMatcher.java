/*
 * Copyright 2002-2012 the original author or authors.
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

package com.sky.server.utils;

import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

public final class MappedRequestPathMatcher {

	private final String[] includePatterns;

	private final String[] excludePatterns;

	private PathMatcher pathMatcher;


	public MappedRequestPathMatcher(PathMatcher pathMatcher, String[] includePatterns) {
		this(pathMatcher, includePatterns, null);
	}

	public MappedRequestPathMatcher(PathMatcher pathMatcher, String[] includePatterns, String[] excludePatterns) {
		this.pathMatcher = pathMatcher;
		this.includePatterns = includePatterns;
		this.excludePatterns = excludePatterns;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	public PathMatcher getPathMatcher() {
		return this.pathMatcher;
	}

	public String[] getPathPatterns() {
		return this.includePatterns;
	}

	public boolean matches(HttpServletRequest request, PathMatcher pathMatcher) {
		PathMatcher pathMatcherToUse = (this.pathMatcher != null) ? this.pathMatcher : pathMatcher;
		if (this.excludePatterns != null) {
			for (String pattern : this.excludePatterns) {
				if (pathMatcherToUse.match(pattern, getRequestPath(request))) {
					return false;
				}
			}
		}
		if (this.includePatterns == null) {
			return true;
		}
		else {
			for (String pattern : this.includePatterns) {
				if (pathMatcherToUse.match(pattern, getRequestPath(request))) {
					return true;
				}
			}
			return false;
		}
	}

	public boolean matches(HttpServletRequest request) {
		return matches(request, null);
	}

	private String getRequestPath(HttpServletRequest request) {
		String url = request.getServletPath();

		if (request.getPathInfo() != null) {
			url += request.getPathInfo();
		}

		return url;
	}
}
