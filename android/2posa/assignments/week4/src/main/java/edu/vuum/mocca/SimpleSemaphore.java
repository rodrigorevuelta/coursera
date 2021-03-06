package edu.vuum.mocca;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore implementation using Java a ReentrantLock and a
 *        ConditionObject. It must implement both "Fair" and "NonFair" semaphore semantics, just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
    ReentrantLock lock;

    /**
     * Define a ConditionObject to wait while the number of permits is 0.
     */
    // TODO - you fill in here
    Condition co;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here. Make sure that this data member will
    // ensure its values aren't cached by multiple Threads.
    private final SimpleAtomicLong sal;

    public SimpleSemaphore(final int permits, final boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
        this.lock = new ReentrantLock(fair);
        this.co = this.lock.newCondition();
        this.sal = new SimpleAtomicLong(permits);
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
        this.lock.lock();
        while (this.sal.get() == 0) {
            this.co.await();
        }
        this.sal.decrementAndGet();
        this.lock.unlock();
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.
        this.lock.lock();
        while (this.sal.get() == 0) {
            try {
                this.co.await();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.sal.decrementAndGet();
        this.lock.unlock();
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here.
        this.lock.lock();
        try {
            this.sal.incrementAndGet();
            this.co.signal();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here by changing null to the appropriate
        return (int) this.sal.get();
    }
}
