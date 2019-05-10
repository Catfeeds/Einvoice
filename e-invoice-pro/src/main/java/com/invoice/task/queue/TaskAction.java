package com.invoice.task.queue;

import com.invoice.task.queue.Task;

public interface TaskAction<P, R> {
	public R obtainData(Task<P, R> task) throws Exception;
}