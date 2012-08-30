/*
 * Distributed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.siyapath.utils;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeInfo;
import org.siyapath.service.NodeData;

public class CommonUtilsTest extends TestCase {
    private static final Log log = LogFactory.getLog(CommonUtilsTest.class);


    public void testSerialize() throws Exception {
        log.info("Starting test: CommonUtils.serialize");
        NodeInfo input = new NodeInfo(8425,"127.0.0.1", 9000);
        NodeData output = CommonUtils.serialize(input);
        NodeData expectedOutput = new NodeData(8425,"127.0.0.1", 9000);
        assertEquals(output, expectedOutput);
        log.info("Serialize test is successful");
    }

    public void testDeSerialize() throws Exception {
        log.info("Starting test: CommonUtils.deSerialize");
        NodeData input = new NodeData(8425,"127.0.0.1", 9000);
        NodeInfo expectedOutput = new NodeInfo(8425,"127.0.0.1", 9000);
        NodeInfo output = CommonUtils.deSerialize(input);
        assertEquals(output, expectedOutput);
        log.info("Deserialize test is successful");
    }
}
