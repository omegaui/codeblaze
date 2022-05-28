/*
 * Copyright (C) 2022 Omega UI

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package omegaui.codeblaze.io;
/*
 * Provides App Release Data.
 */
public final class AppDataProvider {
	
	/**
	 * Returns the full qualified app release title
	 */
	public static final String releaseTitle(){
		return "codeblaze-v" + appVersion() + "-" + buildState();
	}
	
	/**
	 * Returns the App Version Semantic with build state
	 */
	public static final String appVersionSemantic(){
		return appVersion() + "-" + buildState();
	}

	/**
	 * Returns the App Version
	 */
	public static final float appVersion(){
		return 1.0f;
	}

	/**
	 * Returns the build state of the current instance (alpha, stable, unstable, beta)
	 */
	public static final String buildState(){
		return "alpha";
	}
	
}
