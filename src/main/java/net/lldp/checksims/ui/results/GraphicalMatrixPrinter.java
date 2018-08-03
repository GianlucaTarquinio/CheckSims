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
 * Copyright (c) 2014-2016 Ted Meyer, Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */
package net.lldp.checksims.ui.results;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.similaritymatrix.SimilarityMatrix;
import net.lldp.checksims.algorithm.similaritymatrix.output.MatrixPrinter;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.ui.results.mview.QuickSortableMatrixViewer;

/**
 * 
 * @author ted
 *
 */
public class GraphicalMatrixPrinter implements MatrixPrinter
{
    @Override
    public String getName()
    {
        return "GraphicalMatrixPrinter";
    }

    @Override
    public String printMatrix(SimilarityMatrix matrix) throws InternalAlgorithmError
    {
        SortableMatrix sm = new SortableMatrix(matrix);
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(true);
        
        Set<Submission> invalidSubs = matrix.getInvalidSubmissions();
        if(invalidSubs != null) {
        		String invalidSubsStr = "Invalid submissions: ";
	        for(Submission s : invalidSubs) {
	        		invalidSubsStr += s.getName() + ", ";
	        }
	        if(invalidSubs.size() > 0) {
	        		invalidSubsStr = invalidSubsStr.substring(0, invalidSubsStr.length() - 2);
	        }
	        
	        JTextArea invalidSubsList = new JTextArea();
	        invalidSubsList.setEditable(false);
	        invalidSubsList.setLineWrap(true);
	        invalidSubsList.setText(invalidSubsStr);
	        invalidSubsList.setBorder(new EmptyBorder(5, 5, 5, 5));
	        
	        f.add(invalidSubsList, BorderLayout.SOUTH);
        }
        
        f.add(new ScrollViewer(matrix, new QuickSortableMatrixViewer(sm), f), BorderLayout.CENTER);
        f.pack();
        
        f.setVisible(true);
        
        return "see window for details";
    }

}
