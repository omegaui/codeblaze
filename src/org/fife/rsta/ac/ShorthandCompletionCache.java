/*
 * 07/22/2012
 *
 * Copyright (C) 2012 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE.md file for details.
 */
package org.fife.rsta.ac;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fife.ui.autocomplete.AbstractCompletionProvider;
import org.fife.ui.autocomplete.Completion;


/**
 * A cache to store completions for Template completions and Comment
 * completions.  Template completions should extend
 * <code>TemplateCompletion</code> that uses parameterized variables/values.<p>
 *
 * While template completion example:
 * <pre>
 * while --&gt; while(condition) {
 *              //cursor here
 *           }
 * </pre>
 *
 * Comment completion example:
 * <pre>
 * null --&gt; &lt;code&gt;null&lt;/code&gt;
 * </pre>
 *
 * This is really a convenient place to store these types of completions that
 * are re-used.
 *
 * @author Steve
 */
public class ShorthandCompletionCache {

	private List<Completion> shorthandCompletion;
	private List<Completion> commentCompletion;

	private AbstractCompletionProvider templateProvider;
	private AbstractCompletionProvider commentProvider;


	public ShorthandCompletionCache(AbstractCompletionProvider templateProvider,
			AbstractCompletionProvider commentProvider) {
		shorthandCompletion = new ArrayList<>();
		commentCompletion = new ArrayList<>();
		this.templateProvider = templateProvider;
		this.commentProvider = commentProvider;
	}

	/**
	 * Adds a shorthand completion to this cache.
	 *
	 * @param completion The completion to add.
	 * @see #removeShorthandCompletion(Completion)
	 */
	public void addShorthandCompletion(Completion completion) {
		addSorted(shorthandCompletion, completion);
	}


	private static void addSorted(List<Completion> list,
                                  Completion completion) {
		int index = Collections.binarySearch(list, completion);
		if (index<0) {
			// index = -insertion_point - 1
			index = -(index+1);
		}
		list.add(index, completion);
	}


	public List<Completion> getShorthandCompletions() {
		return shorthandCompletion;
	}

	/**
	 * Removes a shorthand completion from this cache.
	 *
	 * @param completion The completion to remove.
	 * @see #addShorthandCompletion(Completion)
	 */
	public void removeShorthandCompletion(Completion completion) {
		shorthandCompletion.remove(completion);
	}

	/**
	 * Removes all completions from this cache.
	 */
	public void clearCache() {
		shorthandCompletion.clear();
	}

	/**
	 * Adds a comment completion to this cache.
	 *
	 * @param completion The completion to add.
	 * @see #removeCommentCompletion(Completion)
	 */
	public void addCommentCompletion(Completion completion) {
		addSorted(commentCompletion, completion);
	}

	public List<Completion> getCommentCompletions() {
		return commentCompletion;
	}

	/**
	 * Removes a specific comment completion from this cache.
	 *
	 * @param completion The completion to remove.
	 * @see #addCommentCompletion(Completion)
	 */
	public void removeCommentCompletion(Completion completion) {
		commentCompletion.remove(completion);
	}

	public AbstractCompletionProvider getTemplateProvider() {
		return templateProvider;
	}

	public AbstractCompletionProvider getCommentProvider() {
		return commentProvider;
	}

}
