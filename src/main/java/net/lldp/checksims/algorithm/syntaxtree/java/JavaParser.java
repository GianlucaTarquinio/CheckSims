/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 * Copyright (c) 2015 Ted Meyer and Michael Andrews
 */
package net.lldp.checksims.algorithm.syntaxtree.java;

import org.apache.commons.lang3.tuple.Pair;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.algorithm.syntaxtree.ASTSimilarityDetector;
import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.SubmissionParser;
import net.lldp.checksims.submission.Submission;

/**
 * 
 * @author ted
 *
 * An algorithm to compare java submissions
 */
public class JavaParser extends SimilarityDetector<AST>
{
    /**
     * getInstance required by reflective instantiator
     * @return
     */
    public static JavaParser getInstance()
    {
        return new JavaParser(); // TODO: make a singleton later? NAH
    }
    
    @Override
    public String getName()
    {
        return "javaparser";
    }

    @Override
    public SubmissionPercentableCalculator<AST> getPercentableCalculator()
    {
        return new SubmissionParser(new JavaSyntaxParser());
    }

    @Override
    public AlgorithmResults detectSimilarity(Pair<Submission, Submission> ab, AST rft, AST comt)
    {
        return ASTSimilarityDetector.detectSimilarity(ab, rft, comt);
    }
    
    @Override
    public String getDefaultGlobPattern()
    {
        return "*.java";
    }
}
