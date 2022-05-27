/*
 * 03/21/2010
 *
 * Copyright (C) 2010 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSTALanguageSupport.License.txt file for details.
 */
package org.fife.rsta.ac.java.rjc.lang;


/**
 * Represents an annotation.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Annotation {

	private Type type;


	/**
	 * Constructor.
	 *
	 * @param type The type.
	 */
	public Annotation(Type type) {
		this.type = type;
	}


	@Override
	public String toString() {
		return "@" + type.toString();
	}


}
