/*
 * TestRegExp.java
 *
 * This work is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * As a special exception, the copyright holders of this library give
 * you permission to link this library with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also meet,
 * for each linked independent module, the terms and conditions of the
 * license of that module. An independent module is a module which is
 * not derived from or based on this library. If you modify this
 * library, you may extend this exception to your version of the
 * library, but you are not obligated to do so. If you do not wish to
 * do so, delete this exception statement from your version.
 *
 * Copyright (c) 2003 Per Cederberg. All rights reserved.
 */

package net.percederberg.grammatica.parser.re;

import junit.framework.TestCase;

/**
 * A test case for the RegExp class.
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  1.0
 */
public class TestRegExp extends TestCase {

    /**
     * The ASCII alphabet characters.
     */
    private static final String ASCII_ALPHABET =
        "ABCDEFGHIJKLMNOPQRSTUNWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * A set of normal characters from ISO-8859-1 .
     */
    private static final String LATIN_1_CHARACTERS =
        "�����������������������������������������������"; 
    
    /**
     * A set of symbol characters from ISO-8859-1 .
     */
    private static final String LATIN_1_SYMBOLS = 
        "�!#�%&/=`'�@�~-_,:;�ޮ�߫��������";

    /**
     * A set of digit characters.
     */
    private static final String DIGITS =
        "0123456789";

    /**
     * A set of whitespace characters.
     */
    private static final String WHITESPACE =
        " \t\n\r\f\r\n\u000B";

    /**
     * Creates a new test case.
     * 
     * @param name           the test case name
     */
    public TestRegExp(String name) {
        super(name);
    }

    /**
     * Tests various regular expression syntax errors.
     */
    public void testSyntaxErrors() {
        failCreateRegExp("");
        failCreateRegExp("?");
        failCreateRegExp("*");
        failCreateRegExp("+");
        failCreateRegExp("{0}");
        failCreateRegExp("(");
        failCreateRegExp(")");
        failCreateRegExp("[ (])");
        failCreateRegExp("+|*");
    }

    /**
     * Tests matching of plain characters.
     */
    public void testCharacters() {
        matchRegExp(ASCII_ALPHABET, ASCII_ALPHABET);
        matchRegExp(LATIN_1_CHARACTERS, LATIN_1_CHARACTERS);
        matchRegExp(LATIN_1_SYMBOLS, LATIN_1_SYMBOLS);
        matchRegExp(DIGITS, DIGITS);
        matchRegExp(WHITESPACE, WHITESPACE);
    }
    
    /**
     * Tests matching of special characters.
     */
    public void testSpecialCharacters() {
        matchRegExp(".*", ASCII_ALPHABET);
        matchRegExp(".*", LATIN_1_CHARACTERS);
        matchRegExp(".*", LATIN_1_SYMBOLS);
        matchRegExp(".*", DIGITS);
        matchRegExp(".*", " \t");
        failMatchRegExp(".+", "\n");
        failMatchRegExp(".+", "\r");
        failMatchRegExp(".+", "\r\n");
        failMatchRegExp(".+", "\u0085");
        failMatchRegExp(".+", "\u2028");
        failMatchRegExp(".+", "\u2029");
        failCreateRegExp("^");
        failCreateRegExp("$");
    }

    /**
     * Tests matching of character set escape sequences.
     */
    public void testCharacterEscapes() {
        matchRegExp("\\d+", DIGITS);
        failMatchRegExp("\\d+", ASCII_ALPHABET);
        failMatchRegExp("\\d+", WHITESPACE);
        matchRegExp("\\D+", ASCII_ALPHABET);
        matchRegExp("\\D+", WHITESPACE);
        failMatchRegExp("\\D+", DIGITS);
        matchRegExp("\\s+", WHITESPACE);
        failMatchRegExp("\\s+", ASCII_ALPHABET);
        matchRegExp("\\S+", ASCII_ALPHABET);
        failMatchRegExp("\\S+", WHITESPACE);
        matchRegExp("\\w+", ASCII_ALPHABET);
        matchRegExp("\\w+", DIGITS);
        matchRegExp("\\w+", "_");
        failMatchRegExp("\\w+", WHITESPACE);
        failMatchRegExp("\\w+", LATIN_1_CHARACTERS);
        failMatchRegExp("\\W+", ASCII_ALPHABET);
        failMatchRegExp("\\W+", DIGITS);
        failMatchRegExp("\\W+", "_");
        matchRegExp("\\W+", WHITESPACE);
        matchRegExp("\\W+", LATIN_1_CHARACTERS);
    }
    
    /**
     * Tests matching of symbol escape sequences.
     */
    public void testSymbolEscapes() {
        matchRegExp("\\\\", "\\");
        matchRegExp("\\\"", "\"");
        matchRegExp("\\'", "'");
        matchRegExp("\\.", ".");
        matchRegExp("\\*", "*");
        matchRegExp("\\+", "+");
        matchRegExp("\\?", "?");
        matchRegExp("\\(", "(");
        matchRegExp("\\)", ")");
        matchRegExp("\\{", "{");
        matchRegExp("\\}", "}");
        matchRegExp("\\[", "[");
        matchRegExp("\\]", "]");
        matchRegExp("\\@", "@");
        matchRegExp("\\<", "<");
        matchRegExp("\\>", ">");
        matchRegExp("\\$", "$");
        matchRegExp("\\%", "%");
        matchRegExp("\\&", "&");
    }
     
    /**
     * Tests matching of control escape sequences.
     */
    public void testControlEscapes() {
        matchRegExp("\\t", "\t");
        matchRegExp("\\n", "\n");
        matchRegExp("\\r", "\r");
        matchRegExp("\\f", "\f");
        matchRegExp("\\a", "\u0007");
        matchRegExp("\\e", "\u001B");
    }

    /**
     * Tests matching of octal escape sequences.
     */
    public void testOctalEscapes() {
        failCreateRegExp("\\0");
        matchRegExp("\\01", "\01");
        matchRegExp("\\012", "\012");
        matchRegExp("\\0101", "A");
        matchRegExp("\\01174", "O4");
        matchRegExp("\\0117a", "Oa");
        matchRegExp("\\018", "\018");
        matchRegExp("\\0118", "\0118");
        failCreateRegExp("\\08");
        failCreateRegExp("\\043");
        failCreateRegExp("\\0432");
    }

    /**
     * Tests matching of hexadecimal escape sequences.
     */
    public void testHexEscapes() {
        failCreateRegExp("\\x");
        failCreateRegExp("\\x1");
        failCreateRegExp("\\x1g");
        matchRegExp("\\x41", "A");
        matchRegExp("\\x4f", "O");
        matchRegExp("\\xABC", "�C");
    }

    /**
     * Tests matching of unicode escape sequences.
     */
    public void testUnicodeEscapes() {
        failCreateRegExp("\\u");
        failCreateRegExp("\\u1");
        failCreateRegExp("\\u11");
        failCreateRegExp("\\u111");
        failCreateRegExp("\\u111g");
        matchRegExp("\\u0041", "A");
        matchRegExp("\\u004f", "O");
        matchRegExp("\\u00ABC", "�C");
    }

    /**
     * Tests matching of invalid escape characters.
     */
    public void testInvalidEscapes() {
        failCreateRegExp("\\A");
        failCreateRegExp("\\B");
        failCreateRegExp("\\C");
        failCreateRegExp("\\E");
        failCreateRegExp("\\F");
        failCreateRegExp("\\G");
        failCreateRegExp("\\H");
        failCreateRegExp("\\I");
        failCreateRegExp("\\J");
        failCreateRegExp("\\K");
        failCreateRegExp("\\L");
        failCreateRegExp("\\M");
        failCreateRegExp("\\N");
        failCreateRegExp("\\O");
        failCreateRegExp("\\P");
        failCreateRegExp("\\Q");
        failCreateRegExp("\\R");
        failCreateRegExp("\\T");
        failCreateRegExp("\\U");
        failCreateRegExp("\\V");
        failCreateRegExp("\\X");
        failCreateRegExp("\\Y");
        failCreateRegExp("\\Z");
        failCreateRegExp("\\b");
        failCreateRegExp("\\c");
        failCreateRegExp("\\g");
        failCreateRegExp("\\h");
        failCreateRegExp("\\i");
        failCreateRegExp("\\j");
        failCreateRegExp("\\k");
        failCreateRegExp("\\l");
        failCreateRegExp("\\m");
        failCreateRegExp("\\o");
        failCreateRegExp("\\p");
        failCreateRegExp("\\q");
        failCreateRegExp("\\u");
        failCreateRegExp("\\v");
        failCreateRegExp("\\y");
        failCreateRegExp("\\z");
    }

    /**
     * Tests matching of character sets.
     */
    public void testCharacterSet() {
        matchRegExp("[ab]", "a");
        matchRegExp("[ab]", "b");
        failMatchRegExp("[ab]", "c");
        failMatchRegExp("[^ab]", "a");
        failMatchRegExp("[^ab]", "b");
        matchRegExp("[^ab]", "c");
        matchRegExp("[A-Za-z]+", ASCII_ALPHABET);
        failMatchRegExp("[A-Za-z]+", DIGITS);
        failMatchRegExp("[A-Za-z]+", WHITESPACE);
        failMatchRegExp("[^A-Za-z]+", ASCII_ALPHABET);
        matchRegExp("[^A-Za-z]+", DIGITS);
        matchRegExp("[^A-Za-z]+", WHITESPACE);
        matchRegExp("[.]", ".");
        failMatchRegExp("[.]", "a");
        matchRegExp("[a-]+", "a-");
        matchRegExp("[-a]+", "a-");
        matchRegExp("[a-]+", "ab", "a");
        matchRegExp("[ \\t\\n\\r\\f\\x0B]*", WHITESPACE);
    }

    /**
     * Tests matching of various greedy quantifiers. 
     */
    public void testGreedyQuantifiers() {
        matchRegExp("a?", "");
        matchRegExp("a?", "a");
        matchRegExp("a?", "aaaa", "a");
        matchRegExp("a*", "");
        matchRegExp("a*", "aaaa");
        failMatchRegExp("a+", "");
        matchRegExp("a+", "a");
        matchRegExp("a+", "aaaa");
        failCreateRegExp("a{0}");
        failMatchRegExp("a{3}", "aa");
        matchRegExp("a{3}", "aaa");
        matchRegExp("a{3}", "aaaa", "aaa");
        failMatchRegExp("a{3,}", "aa");
        matchRegExp("a{3,}", "aaa");
        matchRegExp("a{3,}", "aaaaa");
        failMatchRegExp("a{2,3}", "a");
        matchRegExp("a{2,3}", "aa");
        matchRegExp("a{2,3}", "aaa");
        matchRegExp("a{2,3}", "aaaa", "aaa");
    }
    
    /**
     * Tests matching of various reluctant quantifiers. 
     */
    public void testReluctantQuantifiers() {
        matchRegExp("a??", "");
        matchRegExp("a??", "a", "");
        matchRegExp("a*?", "");
        matchRegExp("a*?", "aaaa", "");
        failMatchRegExp("a+?", "");
        matchRegExp("a+?", "a");
        matchRegExp("a+?", "aaaa", "a");
        failMatchRegExp("a{3}?", "aa");
        failCreateRegExp("a{0}?");
        matchRegExp("a{3}?", "aaa");
        matchRegExp("a{3}?", "aaaa", "aaa");
        failMatchRegExp("a{3,}?", "aa");
        matchRegExp("a{3,}?", "aaa");
        matchRegExp("a{3,}?", "aaaaa", "aaa");
        failMatchRegExp("a{2,3}?", "a");
        matchRegExp("a{2,3}?", "aa");
        matchRegExp("a{2,3}?", "aaa", "aa");
        matchRegExp("a{2,3}?", "aaaa", "aa");
    }

    /**
     * Tests matching of various possessive quantifiers. 
     */
    public void testPossessiveQuantifiers() {
        matchRegExp("a?+", "");
        matchRegExp("a?+", "a");
        matchRegExp("a*+", "");
        matchRegExp("a*+", "aaaa");
        failMatchRegExp("a++", "");
        matchRegExp("a++", "a");
        matchRegExp("a++", "aaaa");
        failMatchRegExp("a{3}+", "aa");
        failCreateRegExp("a{0}+");
        matchRegExp("a{3}+", "aaa");
        matchRegExp("a{3}+", "aaaa", "aaa");
        failMatchRegExp("a{3,}+", "aa");
        matchRegExp("a{3,}+", "aaa");
        matchRegExp("a{3,}+", "aaaaa", "aaaaa");
        failMatchRegExp("a{2,3}+", "a");
        matchRegExp("a{2,3}+", "aa");
        matchRegExp("a{2,3}+", "aaa");
        matchRegExp("a{2,3}+", "aaaa", "aaa");
    }
    
    /**
     * Tests the backtracking over the quantifier matches.
     */
    public void testQuantifierBacktracking() {
        matchRegExp("a?a", "a");
        matchRegExp("a*a", "aaaa");
        matchRegExp("a*aaaa", "aaaa");
        failMatchRegExp("a*aaaa", "aaa");
        matchRegExp("a+a", "aaaa");
        matchRegExp("a+aaa", "aaaa");
        failMatchRegExp("a+aaaa", "aaaa");
        failMatchRegExp("a{3,}a", "aaa");
        matchRegExp("a{3,}a", "aaaaa");
        matchRegExp("a{2,3}a", "aaa");
        failMatchRegExp("a{2,3}a", "aa");
        matchRegExp("a??b", "ab");
        matchRegExp("a*?b", "aaab");
        matchRegExp("a+?b", "aaab");
        matchRegExp("a{3,}?b", "aaaaab");
        matchRegExp("a{2,3}?b", "aaab");
        failMatchRegExp("a?+a", "a");
        failMatchRegExp("a*+a", "aaaa");
        failMatchRegExp("a++a", "aaaa");
        failMatchRegExp("a{3,}+a", "aaaaa");
        failMatchRegExp("a{2,3}+a", "aaa");
    }

    /**
     * Tests the quantifier backtracking for stack overflows.
     * (Bug #3632)
     */
    public void testQuantifierStackOverflow() {
        StringBuffer  buffer = new StringBuffer();
        String        str;
        
        for (int i = 0; i < 4096; i++) {
            buffer.append("a");
        }
        str = buffer.toString();
        matchRegExp("a*" + str, str);
        failMatchRegExp("a*a" + str, str);
        matchRegExp("a*?b", str + "b");
        failMatchRegExp("a*?b", str);
        matchRegExp("a*+", str);
        failMatchRegExp("a*+a", str);
    }

    /**
     * Tests the quantifier backtracking state memory. This tests 
     * changes the character buffer used by the matcher object and
     * tests if the quantifier state is properly reset. (Bug #3653)
     */
    public void testCharBufferAppend() {
        Matcher     m;
        CharBuffer  buffer;
        
        buffer = new CharBuffer();
        m = createRegExp("a*aa").matcher(buffer);
        buffer.append("a");
        if (m.matchFromBeginning()) {
            fail("found invalid match '" + m.toString() + 
                 "' to regexp 'a*aa' in input '" + buffer + "'");
        }
        buffer.append("aaa");
        if (!m.matchFromBeginning()) {
            fail("couldn't match '" + buffer + "' to regexp 'a*aa'");
        } else if (!buffer.toString().equals(m.toString())) {
            fail("incorrect match for 'a*aa', found: '" + 
                 m.toString() + "', expected: '" + buffer + "'");
        }

        buffer = new CharBuffer();
        m = createRegExp("a*?b").matcher(buffer);
        buffer.append("aaa");
        if (m.matchFromBeginning()) {
            fail("found invalid match '" + m.toString() + 
                 "' to regexp 'a*?b' in input '" + buffer + "'");
        }
        buffer.append("aab");
        if (!m.matchFromBeginning()) {
            fail("couldn't match '" + buffer + "' to regexp 'a*?b'");
        } else if (!buffer.toString().equals(m.toString())) {
            fail("incorrect match for 'a*?b', found: '" + 
                 m.toString() + "', expected: '" + buffer + "'");
        }
    }

    /**
     * Tests matching of various logical operators. 
     */
    public void testLogicalOperators() {
        matchRegExp("a|ab|b", "a");
        matchRegExp("a|ab|b", "b");
        matchRegExp("a|ab|b", "ab");
        matchRegExp("(ab)", "ab");
        matchRegExp("(a)(b)", "ab");
    }

    /**
     * Tests the regular expression operator associativity. 
     */
    public void testAssociativity() {
        matchRegExp("ab?c", "ac");
        failMatchRegExp("ab?c", "c");
        matchRegExp("aa|b", "aa");
        failMatchRegExp("aa|b", "ab");
        matchRegExp("ab|bc", "ab");
        matchRegExp("ab|bc", "bc");
        matchRegExp("(a|b)c", "ac");
        matchRegExp("(a|b)c", "bc");
        failMatchRegExp("(a|b)c", "abc");
    }

    /**
     * Tests matching of various complex expressions. 
     */
    public void testComplex() {
        matchRegExp("a*-", "aa-");
        matchRegExp("([) ])+", ") ))");
        matchRegExp("a*a*aa", "aa");
        matchRegExp("(a*)*aa", "aaaa");
        matchRegExp("a+a+aa", "aaaa");
        matchRegExp("(a+)+aa", "aaaa");
    }

    /**
     * Creates a new regular expression. If the expression couldn't be
     * parsed correctly, a test failure will be reported.
     * 
     * @param pattern        the pattern to use
     * 
     * @return the newly created regular expression
     */
    private RegExp createRegExp(String pattern) {
        try {
            return new RegExp(pattern);
        } catch (RegExpException e) {
            fail("couldn't create regular expression '" + pattern +
                 "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks that a specified regular expression pattern is 
     * erroneous. If the regular expression class doesn't detect the
     * error, a test failure will be reported.
     * 
     * @param pattern        the pattern to check
     */
    private void failCreateRegExp(String pattern) {
        try {
            new RegExp(pattern);
            fail("regular expression '" + pattern + "' could be " +
                 "created although it isn't valid");
        } catch (RegExpException e) {
            // Failure was expected
        }
    }
    
    /**
     * Checks that a specified regular expression matches an input
     * string. The whole input string must be matched by the regular
     * expression. This method will report a failure if the regular 
     * expression couldn't be created or if the match wasn't exact.
     * 
     * @param pattern        the regular expression to check
     * @param input          the input and match string
     */
    private void matchRegExp(String pattern, String input) {
        matchRegExp(pattern, input, input);
    }
    
    /**
     * Checks that a specified regular expression matches an input
     * string. The exact match is compared to a specified match. This
     * method will report a failure if the regular expression couldn't
     * be created or if the match wasn't exact.
     * 
     * @param pattern        the regular expression to check
     * @param input          the input string
     * @param match          the match string
     */
    private void matchRegExp(String pattern, String input, String match) {
        RegExp   r = createRegExp(pattern);
        Matcher  m = r.matcher(input);
        
        if (!m.matchFromBeginning()) {
            fail("couldn't match '" + input + "' to regexp '" + 
                 pattern + "'");
        } else if (!match.equals(m.toString())) {
            fail("incorrect match for '" + pattern + "', found: '" + 
                 m.toString() + "', expected: '" + match + "'");
        }
    }
    
    /**
     * Checks that a specified regular expression does not match the
     * input string. This method will report a failure if the regular 
     * expression couldn't be created or if a match was found.
     * 
     * @param pattern        the regular expression to check
     * @param input          the input and match string
     */
    private void failMatchRegExp(String pattern, String input) {
        RegExp   r = createRegExp(pattern);
        Matcher  m = r.matcher(input);
        
        if (m.matchFromBeginning()) {
            fail("found invalid match '" + m.toString() + 
                 "' to regexp '" + pattern + "' in input '" + 
                 input + "'");
        }
    }
}