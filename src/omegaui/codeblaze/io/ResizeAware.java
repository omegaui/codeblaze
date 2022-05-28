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
 * ResizeAware interface is created to simplify how a component in a custom layout resizes.
 * Should be called in the layout() of the Component.
 * e.g:
 	@Override
 	public void layout(){
 		super.layout();
 		manageBounds();
 	}
 */
public interface ResizeAware {

	/**
	 * All component resize code should be written in here.
	 * e.g when you implement this interface:
	 	@Override
	 	public void manageBounds(){
	 		comp.setBounds(getWidth()/2 - width/2, getHeight()/2 - height/2, widht, height);
 		}
	 */
	void manageBounds();
}
