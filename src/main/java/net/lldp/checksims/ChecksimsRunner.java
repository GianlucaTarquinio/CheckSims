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
 * Copyright (c) 2014-2015 Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */

package net.lldp.checksims;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.AlgorithmRunner;
import net.lldp.checksims.algorithm.preprocessor.PreprocessSubmissions;
import net.lldp.checksims.algorithm.preprocessor.SubmissionPreprocessor;
import net.lldp.checksims.algorithm.similaritymatrix.SimilarityMatrix;
import net.lldp.checksims.algorithm.similaritymatrix.output.MatrixPrinter;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.util.PairGenerator;
import net.lldp.checksims.util.threading.ParallelAlgorithm;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * CLI Entry point and main public API endpoint for Checksims.
 */
public final class ChecksimsRunner {

    private ChecksimsRunner() {}

    /**
     * CLI entrypoint of Checksims.
     *
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        try {
            ChecksimsCommandLine.runCLI(args);
        } catch(ParseException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        } catch(ChecksimsException e) {
            System.err.println(e.toString());
            if(e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().toString());
            }
            // Print the stack trace for internal exceptions, they may be serious
            e.printStackTrace();
            System.exit(-1);
        } catch(IOException e) {
            System.err.println("I/O Error!");
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        System.exit(0);
    }

    /**
     * Get current version.
     *
     * @return Current version of Checksims
     */
    public static String getChecksimsVersion() throws ChecksimsException {
        InputStream resource = ChecksimsCommandLine.class.getResourceAsStream("version.txt");

        if(resource == null) {
            throw new ChecksimsException("Error obtaining resource for version!");
        }

        try {
            return IOUtils.toString(resource);
        } catch (IOException e) {
            throw new ChecksimsException("IO Exception reading version: " + e.getMessage(), e);
        }
    }
    
    public static ImmutableMap<String, String> runChecksims(ChecksimsConfig config) throws ChecksimsException {
    		ImmutableMap results;
    		try {
    			results = runChecksims(config, null);
    		} catch(ChecksimsException e) {
    			throw e;
    		}
    		return results;
    }

    /**
     * Main public entrypoint to Checksims. Runs similarity detection according to given configuration.
     *
     * @param config Configuration defining how Checksims will be run
     * @return Map containing output of all output printers requested. Keys are name of output printer.
     * @throws ChecksimsException Thrown on error performing similarity detection
     */
    public static ImmutableMap<String, String> runChecksims(ChecksimsConfig config, ChecksimsConfig backupConfig) throws ChecksimsException {
        checkNotNull(config);

        // Create a logger to log activity
        Logger logs = LoggerFactory.getLogger(ChecksimsRunner.class);

        // Set parallelism
        int threads = config.getNumThreads();
        ParallelAlgorithm.setThreadCount(threads);
        // TODO following line may not be necessary as we no longer use parallel streams?
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "" + threads);

        ImmutableSet<Submission> submissions = config.getSubmissions();

        logs.info("Got " + submissions.size() + " submissions to test.");

        ImmutableSet<Submission> archiveSubmissions = config.getArchiveSubmissions();

        if(!archiveSubmissions.isEmpty()) {
            logs.info("Got " + archiveSubmissions.size() + " archive submissions to test.");
        }

        if(submissions.size() == 0) {
            throw new ChecksimsException("No student submissions were found - cannot run Checksims!");
        }

        // Apply all preprocessors
        for(SubmissionPreprocessor p : config.getPreprocessors()) {
            submissions = ImmutableSet.copyOf(PreprocessSubmissions.process(p, submissions, config.getStatusLogger()));

            if(!archiveSubmissions.isEmpty()) {
                archiveSubmissions = ImmutableSet.copyOf(PreprocessSubmissions.process(p, archiveSubmissions, config.getStatusLogger()));
            }
        }

        if(submissions.size() < 2) {
            throw new ChecksimsException("Did not get at least 2 student submissions! Cannot run Checksims!");
        }

        // Apply algorithm to submissions
        Set<Pair<Submission, Submission>> allPairs = PairGenerator.generatePairsWithArchive(submissions,
                archiveSubmissions);
        Set<AlgorithmResults> results = AlgorithmRunner.runAlgorithm(allPairs, config.getAlgorithm(), config.getStatusLogger());
        
        if (config.isIgnoringInvalid()) {
            Set<Submission> validSubmissions = new HashSet<>();
            Set<Submission> invalidSubmissions = new HashSet<>();
            Set<Submission> validArchivedSubmissions = new HashSet<>();
            Set<Submission> invalidArchivedSubmissions = new HashSet<>();
            Set<AlgorithmResults> validResults = new HashSet<>();
            
            for(Submission s : submissions) {
            		if(s.testFlag("invalid")) {
            			invalidSubmissions.add(s);
            		} else {
            			validSubmissions.add(s);
            		}
            }
            
            for(Submission s : archiveSubmissions) {
	            	if(s.testFlag("invalid")) {
	        			invalidArchivedSubmissions.add(s);
	        		} else {
	        			validArchivedSubmissions.add(s);
	        		}
            }
            
            for(AlgorithmResults ar : results) {
            		if(ar.isValid()) {
            			validResults.add(ar);
            		}
            }
            
            results = validResults;
            
            if(backupConfig != null && invalidSubmissions.size() > 0 || invalidArchivedSubmissions.size() > 0) {
            		threads = backupConfig.getNumThreads();
            		ParallelAlgorithm.shutdownExecutor();
            		ParallelAlgorithm.setThreadCount(threads);
            		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "" + threads);
            		
            		ImmutableSet<Submission> bSubmissions = backupConfig.getSubmissions();
            		logs.info("Got " + bSubmissions.size() + " backup submissions to test.");
            		
            		ImmutableSet<Submission> bArchiveSubmissions = backupConfig.getArchiveSubmissions();
                if(!bArchiveSubmissions.isEmpty()) {
                    logs.info("Got " + bArchiveSubmissions.size() + " backup archive submissions to test.");
                }
                
                for(SubmissionPreprocessor p : backupConfig.getPreprocessors()) {
                    bSubmissions = ImmutableSet.copyOf(PreprocessSubmissions.process(p, bSubmissions, backupConfig.getStatusLogger()));
                    if(!bArchiveSubmissions.isEmpty()) {
                        bArchiveSubmissions = ImmutableSet.copyOf(PreprocessSubmissions.process(p, bArchiveSubmissions, backupConfig.getStatusLogger()));
                    }
                }
                
                if(bSubmissions.size() > 1) {              
                		Set<Pair<Submission, Submission>> bPairs = PairGenerator.generatePairsWithArchive(bSubmissions,
                        bArchiveSubmissions);
                		Set<AlgorithmResults> bResults = AlgorithmRunner.runAlgorithm(bPairs, backupConfig.getAlgorithm(), backupConfig.getStatusLogger());                
	                
                		for(AlgorithmResults r : bResults) {
	                		if(r.includes(invalidSubmissions) || r.includes(invalidArchivedSubmissions)) {
	                			results.add(r);
	                		}
	                }
                		
                		validSubmissions.addAll(invalidSubmissions);
                		validArchivedSubmissions.addAll(invalidArchivedSubmissions);
                		
                		submissions = ImmutableSet.copyOf(validSubmissions);
                		archiveSubmissions = ImmutableSet.copyOf(validArchivedSubmissions);
                }
            } else {            
            		submissions = ImmutableSet.copyOf(validSubmissions);
            		archiveSubmissions = ImmutableSet.copyOf(validArchivedSubmissions);
            }
        }
        
        SimilarityMatrix resultsMatrix = SimilarityMatrix.generateMatrix(submissions, archiveSubmissions, results);
        

        // All parallel jobs are done, shut down the parallel executor
        ParallelAlgorithm.shutdownExecutor();
        config.getStatusLogger().end();

        Map<String, String> outputMap = new HashMap<>();

        // Output using all output printers
        for(MatrixPrinter p : config.getOutputPrinters()) {
            logs.info("Generating " + p.getName() + " output");

            outputMap.put(p.getName(), p.printMatrix(resultsMatrix));
        }
        
        ChecksimsCommandLine.deleteTempFiles();

        return ImmutableMap.copyOf(outputMap);
    }
}
