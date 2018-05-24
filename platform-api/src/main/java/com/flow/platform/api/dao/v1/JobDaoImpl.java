/*
 * Copyright 2018 fir.im
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

package com.flow.platform.api.dao.v1;

import com.flow.platform.api.domain.v1.JobKey;
import com.flow.platform.api.domain.v1.JobV1;
import com.flow.platform.core.dao.AbstractBaseDao;
import com.flow.platform.core.domain.Page;
import com.flow.platform.core.domain.Pageable;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author yang
 */
@Repository(value = "jobDaoV1")
public class JobDaoImpl extends AbstractBaseDao<JobKey, JobV1> implements JobDao {

    @Override
    protected Class<JobV1> getEntityClass() {
        return JobV1.class;
    }

    @Override
    protected String getKeyName() {
        return "key";
    }

    @Override
    public Page<JobV1> listByFlow(Collection<String> flows, Pageable pageable) {
        List<JobV1> jobs = execute(session -> session
            .createQuery("from JobV1 where key.flow in :flows", JobV1.class)
            .setParameter("flows", flows)
            .setFirstResult(pageable.getOffset())
            .setMaxResults(pageable.getSize())
            .getResultList());

        Long totalSize = execute(session -> session
            .createQuery("select count(*) from JobV1 where key.flow = :flows", Long.class)
            .setParameter("flows", flows)
            .uniqueResult());

        return new Page<>(jobs, jobs.size(), pageable.getNumber(), totalSize);
    }

    @Override
    public Page<JobV1> listLatestByFlows(Collection<String> flows, Pageable pageable) {
        return null;
    }

    @Override
    public void deleteByFlow(String flow) {
        execute(session -> {
            return session.createQuery("delete from JobV1 where key.flow = :flow")
                .setParameter("flow", flow)
                .executeUpdate();
        });
    }
}
