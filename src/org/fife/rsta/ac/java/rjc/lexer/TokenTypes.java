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
package org.fife.rsta.ac.java.rjc.lexer;


/**
 * All possible token types returned by this lexer.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface TokenTypes {

	int KEYWORD					= (0x01)<<16;
	int DATA_TYPE				= (0x02|KEYWORD)<<16;
	int IDENTIFIER				= (0x04)<<16;
	int COMMENT					= (0x08)<<16;
	int DOC_COMMENT				= (0x10|COMMENT)<<16;
	int WHITESPACE				= (0x20)<<16;
	int LITERAL					= (0x40)<<16;
	int SEPARATOR				= (0x80)<<16;
	int OPERATOR				= (0x100)<<16;
	int ASSIGNMENT_OPERATOR		= (0x200|OPERATOR)<<16;
	int ANNOTATION_START		= (0x400)<<16;
	int ELIPSIS					= (0x800)<<16;

	int KEYWORD_ABSTRACT		= KEYWORD|1;
	int KEYWORD_ASSERT			= KEYWORD|2;
	int KEYWORD_BOOLEAN			= DATA_TYPE|3;
	int KEYWORD_BREAK			= KEYWORD|4;
	int KEYWORD_BYTE			= DATA_TYPE|5;
	int KEYWORD_CASE			= KEYWORD|6;
	int KEYWORD_CATCH			= KEYWORD|7;
	int KEYWORD_CHAR			= DATA_TYPE|8;
	int KEYWORD_CLASS			= KEYWORD|9;
	int KEYWORD_CONST			= KEYWORD|10;
	int KEYWORD_CONTINUE		= KEYWORD|11;
	int KEYWORD_DEFAULT			= KEYWORD|12;
	int KEYWORD_DO				= KEYWORD|13;
	int KEYWORD_DOUBLE			= DATA_TYPE|14;
	int KEYWORD_ELSE			= KEYWORD|15;
	int KEYWORD_ENUM			= KEYWORD|16;
	int KEYWORD_EXTENDS			= KEYWORD|17;
	int KEYWORD_FINAL			= KEYWORD|18;
	int KEYWORD_FINALLY			= KEYWORD|19;
	int KEYWORD_FLOAT			= DATA_TYPE|20;
	int KEYWORD_FOR				= KEYWORD|21;
	int KEYWORD_GOTO			= KEYWORD|22;
	int KEYWORD_IF				= KEYWORD|23;
	int KEYWORD_IMPLEMENTS		= KEYWORD|24;
	int KEYWORD_IMPORT			= KEYWORD|25;
	int KEYWORD_INSTANCEOF		= KEYWORD|26;
	int KEYWORD_INT				= DATA_TYPE|27;
	int KEYWORD_INTERFACE		= KEYWORD|28;
	int KEYWORD_LONG			= DATA_TYPE|29;
	int KEYWORD_NATIVE			= KEYWORD|30;
	int KEYWORD_NEW				= KEYWORD|31;
	int KEYWORD_PACKAGE			= KEYWORD|32;
	int KEYWORD_PRIVATE			= KEYWORD|33;
	int KEYWORD_PROTECTED		= KEYWORD|34;
	int KEYWORD_PUBLIC			= KEYWORD|35;
	int KEYWORD_RETURN			= KEYWORD|36;
	int KEYWORD_SHORT			= DATA_TYPE|37;
	int KEYWORD_STATIC			= KEYWORD|38;
	int KEYWORD_STRICTFP		= KEYWORD|39;
	int KEYWORD_SUPER			= KEYWORD|40;
	int KEYWORD_SWITCH			= KEYWORD|41;
	int KEYWORD_SYNCHRONIZED	= KEYWORD|42;
	int KEYWORD_THIS			= KEYWORD|43;
	int KEYWORD_THROW			= KEYWORD|44;
	int KEYWORD_THROWS			= KEYWORD|45;
	int KEYWORD_TRANSIENT		= KEYWORD|46;
	int KEYWORD_TRY				= KEYWORD|47;
	int KEYWORD_VOID			= KEYWORD|48;
	int KEYWORD_VOLATILE		= KEYWORD|49;
	int KEYWORD_WHILE			= KEYWORD|50;

	int LITERAL_INT				= LITERAL|1;
	int LITERAL_FP				= LITERAL|2;
	int LITERAL_BOOLEAN			= LITERAL|3;
	int LITERAL_CHAR			= LITERAL|4;
	int LITERAL_STRING			= LITERAL|5;
	int LITERAL_NULL			= LITERAL|6;

	int SEPARATOR_LPAREN		= SEPARATOR|1;
	int SEPARATOR_RPAREN		= SEPARATOR|2;
	int SEPARATOR_LBRACE		= SEPARATOR|3;
	int SEPARATOR_RBRACE		= SEPARATOR|4;
	int SEPARATOR_LBRACKET		= SEPARATOR|5;
	int SEPARATOR_RBRACKET		= SEPARATOR|6;
	int SEPARATOR_SEMICOLON		= SEPARATOR|7;
	int SEPARATOR_COMMA			= SEPARATOR|8;
	int SEPARATOR_DOT			= SEPARATOR|9;

	int OPERATOR_EQUALS				= ASSIGNMENT_OPERATOR|1;
	int OPERATOR_GT					= OPERATOR|2;
	int OPERATOR_LT					= OPERATOR|3;
	int OPERATOR_LOGICAL_NOT		= OPERATOR|4;
	int OPERATOR_BITWISE_NOT		= OPERATOR|5;
	int OPERATOR_QUESTION			= OPERATOR|6;
	int OPERATOR_COLON				= OPERATOR|7;
	int OPERATOR_EQUALS_EQUALS		= OPERATOR|8;
	int OPERATOR_LTE				= OPERATOR|9;
	int OPERATOR_GTE				= OPERATOR|10;
	int OPERATOR_NE					= OPERATOR|11;
	int OPERATOR_LOGICAL_AND		= OPERATOR|12;
	int OPERATOR_LOGICAL_OR			= OPERATOR|13;
	int OPERATOR_INCREMENT			= OPERATOR|14;
	int OPERATOR_DECREMENT			= OPERATOR|15;
	int OPERATOR_PLUS				= OPERATOR|16;
	int OPERATOR_MINUS				= OPERATOR|17;
	int OPERATOR_TIMES				= OPERATOR|18;
	int OPERATOR_DIVIDE				= OPERATOR|19;
	int OPERATOR_BITWISE_AND		= OPERATOR|20;
	int OPERATOR_BITWISE_OR			= OPERATOR|21;
	int OPERATOR_BITWISE_XOR		= OPERATOR|22;
	int OPERATOR_MOD				= OPERATOR|23;
	int OPERATOR_LSHIFT				= OPERATOR|24;
	int OPERATOR_RSHIFT				= OPERATOR|25;
	int OPERATOR_RSHIFT2			= OPERATOR|26;
	int OPERATOR_PLUS_EQUALS		= ASSIGNMENT_OPERATOR|27;
	int OPERATOR_MINUS_EQUALS		= ASSIGNMENT_OPERATOR|28;
	int OPERATOR_TIMES_EQUALS		= ASSIGNMENT_OPERATOR|29;
	int OPERATOR_DIVIDE_EQUALS		= ASSIGNMENT_OPERATOR|30;
	int OPERATOR_BITWISE_AND_EQUALS	= ASSIGNMENT_OPERATOR|31;
	int OPERATOR_BITWISE_OR_EQUALS	= ASSIGNMENT_OPERATOR|32;
	int OPERATOR_BITWISE_XOR_EQUALS	= ASSIGNMENT_OPERATOR|33;
	int OPERATOR_MOD_EQUALS			= ASSIGNMENT_OPERATOR|34;
	int OPERATOR_LSHIFT_EQUALS		= ASSIGNMENT_OPERATOR|35;
	int OPERATOR_RSHIFT_EQUALS		= ASSIGNMENT_OPERATOR|36;
	int OPERATOR_RSHIFT2_EQUALS		= ASSIGNMENT_OPERATOR|37;

}
