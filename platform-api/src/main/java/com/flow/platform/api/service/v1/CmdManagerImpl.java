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

package com.flow.platform.api.service.v1;

import com.flow.platform.domain.v1.JobKey;
import com.flow.platform.tree.Cmd;
import com.flow.platform.tree.Node;
import com.flow.platform.tree.YmlEnvs;
import org.springframework.stereotype.Component;

/**
 * @author yang
 */
@Component
public class CmdManagerImpl implements CmdManager {

    @Override
    public Cmd create(JobKey key, Node node, String token) {
        // trans node to cmd
        Cmd cmd = new Cmd();
        cmd.setNodePath(node.getPath());
        cmd.setContent(node.getContent());
        cmd.put(YmlEnvs.TIMEOUT, "100");
        cmd.put(YmlEnvs.WORK_DIR, "/tmp"); //TODO: Working dir
        cmd.put(YmlEnvs.AGENT_TOKEN, token);
        cmd.setJobKey(key);
        return cmd;
    }
}
