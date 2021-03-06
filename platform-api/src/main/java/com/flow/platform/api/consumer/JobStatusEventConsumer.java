/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flow.platform.api.consumer;

import com.flow.platform.api.domain.job.Job;
import com.flow.platform.api.domain.job.JobStatus;
import com.flow.platform.api.events.JobStatusChangeEvent;
import com.flow.platform.api.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;

/**
 * To handle JobStatusChangeEvent and NodeResultStatusChangeEvent
 *
 * @author yang
 */
@Log4j2
public class JobStatusEventConsumer extends JobEventPushHandler implements ApplicationListener<JobStatusChangeEvent> {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private MessageService messageService;

    @Override
    public void onApplicationEvent(JobStatusChangeEvent event) {
        log.debug("Job {} status change event from {} to {}", event.getJob().getId(), event.getFrom(), event.getTo());

        push(event.getJob());

        // async send message TODO:// only send failure message
        if (Job.FAILURE_STATUS.contains(event.getTo())) {
            sendMessage(event.getJob(), event.getTo());
        }
    }

    private void sendMessage(Job job, JobStatus status) {
        taskExecutor.execute(() -> messageService.sendMessage(job, status));
    }
}
