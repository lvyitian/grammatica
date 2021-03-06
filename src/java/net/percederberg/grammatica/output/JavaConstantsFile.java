/*
 * JavaConstantsFile.java
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the BSD license.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * LICENSE.txt file for more details.
 *
 * Copyright (c) 2003-2015 Per Cederberg. All rights reserved.
 */

package net.percederberg.grammatica.output;

import java.io.IOException;
import java.util.HashMap;

import net.percederberg.grammatica.code.java.JavaComment;
import net.percederberg.grammatica.code.java.JavaFile;
import net.percederberg.grammatica.code.java.JavaInterface;
import net.percederberg.grammatica.code.java.JavaVariable;
import net.percederberg.grammatica.parser.ProductionPattern;
import net.percederberg.grammatica.parser.TokenPattern;

/**
 * The Java constants file generator. This class encapsulates all the
 * Java code necessary for creating a constants interface file.
 *
 * @author   Per Cederberg
 * @version  1.0
 */
class JavaConstantsFile {

    /**
     * The interface comment.
     */
    private static final String TYPE_COMMENT =
        "An interface with constants for the parser and tokenizer.";

    /**
     * The token constant comment.
     */
    private static final String TOKEN_COMMENT =
        "A token identity constant.";

    /**
     * The production constant comment.
     */
    private static final String PRODUCTION_COMMENT =
        "A production node identity constant.";

    /**
     * The Java parser generator.
     */
    private JavaParserGenerator gen;

    /**
     * The Java file to write.
     */
    private JavaFile file;

    /**
     * The Java constants interface.
     */
    private JavaInterface ifc;

    /**
     * The mapping from id to constant name. This map contains all
     * tokens and productions added to the file.
     */
    private HashMap constantNames = new HashMap();

    /**
     * Creates a new constants file.
     *
     * @param gen            the parser generator to use
     */
    public JavaConstantsFile(JavaParserGenerator gen) {
        int  modifiers;

        this.gen = gen;
        this.file = gen.createJavaFile();
        if (gen.getPublicAccess()) {
            modifiers = JavaInterface.PUBLIC;
        } else {
            modifiers = JavaInterface.PACKAGE_LOCAL;
        }
        this.ifc = new JavaInterface(modifiers,
                                     gen.getBaseName() + "Constants");
        initializeCode();
    }

    /**
     * Initializes the source code objects.
     */
    private void initializeCode() {
        String  str;

        // Add interface
        file.addInterface(ifc);

        // Add file comment
        str = file.toString() + "\n\n" + gen.getFileComment();
        file.addComment(new JavaComment(JavaComment.BLOCK, str));

        // Add interface comment
        str = TYPE_COMMENT;
        if (gen.getClassComment() != null) {
            str += "\n\n" + gen.getClassComment();
        }
        ifc.addComment(new JavaComment(str));
    }

    /**
     * Adds a token constant definition to this file.
     *
     * @param pattern        the token pattern
     */
    public void addToken(TokenPattern pattern) {
        String        constant;
        JavaVariable  var;
        int           modifiers;

        constant = gen.getCodeStyle().getUpperCase(pattern.getName());
        modifiers = JavaVariable.PUBLIC + JavaVariable.STATIC +
                    JavaVariable.FINAL;
        var = new JavaVariable(modifiers,
                               "int",
                               constant,
                               "" + pattern.getId());
        var.addComment(new JavaComment(TOKEN_COMMENT));
        ifc.addVariable(var);
        constantNames.put(Integer.valueOf(pattern.getId()), constant);
    }

    /**
     * Adds a production constant definition to this file. This method
     * checks if the production pattern has already been added.
     *
     * @param pattern        the production pattern
     */
    public void addProduction(ProductionPattern pattern) {
        String        constant;
        JavaVariable  var;
        int           modifiers;

        if (!pattern.isSynthetic()) {
            constant = gen.getCodeStyle().getUpperCase(pattern.getName());
            modifiers = JavaVariable.PUBLIC + JavaVariable.STATIC +
                        JavaVariable.FINAL;
            var = new JavaVariable(modifiers,
                                   "int",
                                   constant,
                                   "" + pattern.getId());
            var.addComment(new JavaComment(PRODUCTION_COMMENT));
            ifc.addVariable(var);
            constantNames.put(Integer.valueOf(pattern.getId()), constant);
        }
    }

    /**
     * Creates source code for accessing one of the constants in this
     * file.
     *
     * @param id             the node type (pattern) id
     *
     * @return the constant name, or
     *         null if not found
     */
    public String getConstant(int id) {
        String  name = (String) constantNames.get(Integer.valueOf(id));

        if (name == null) {
            return null;
        } else {
            return ifc.toString() + "." + name;
        }
    }

    /**
     * Writes the file source code.
     *
     * @throws IOException if the output file couldn't be created
     *             correctly
     */
    public void writeCode() throws IOException {
        file.writeCode(gen.getCodeStyle());
    }
}
