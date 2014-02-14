/**
 * Copyright 2002 - 2007 the Mogwai Project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.mogwai.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class WorkerQueue {

    private Logger LOGGER = LoggerFactory.getLogger(WorkerQueue.class);

    private int threadCount;

    private PoolWorker[] threads;

    private LinkedList<Runnable> queue = new LinkedList<Runnable>();

    public WorkerQueue(int aThreadCount) {

        threadCount = aThreadCount;
        threads = new PoolWorker[aThreadCount];
        for (int i = 0; i < aThreadCount; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable aRunnable) {
        synchronized (queue) {
            queue.addLast(aRunnable);
            queue.notify();
        }
    }

    public boolean isRunning() {
        for (int i = 0; i < threadCount; i++) {
            if (threads[i].isRunning()) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        synchronized (queue) {
            return queue.size();
        }
    }

    public void shutdown() {
        for (int i = 0; i < threadCount; i++) {
            threads[i].interrupt();
        }
    }

    private class PoolWorker extends Thread {

        private boolean running;

        public PoolWorker() {
            super(WorkerQueue.this.toString() + "#PoolWorker");
        }

        public boolean isRunning() {
            return running;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {

                running = false;

                Runnable theRunnable = null;

                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (Exception e) {
                            // Ignore this
                        }
                    }

                    running = true;
                    theRunnable = queue.removeFirst();
                }

                LOGGER.debug(getName() + " Processing payload");

                try {
                    theRunnable.run();
                } catch (Exception e) {
                    LOGGER.error("Error executing payload", e);
                }

            }
        }
    }
}
