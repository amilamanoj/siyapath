package org.siyapath.client;

/**
 * Thrown by UserHandler when job submission has failed
 */
public class SubmissionFailedException extends Exception {
    public SubmissionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
