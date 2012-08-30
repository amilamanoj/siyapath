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

package org.siyapath.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Is responsible for loading classes given the bytes that make up the class
 * data sent by user
 */
public class TaskClassLoader extends ClassLoader{

    private final Log log = LogFactory.getLog(TaskClassLoader.class);

    /**
     *
     * @param byteArray The bytes that make up the class data.
     * @param name class name
     * @return Class instance specified by byteArray and binary name of the class sent
     * @throws ClassNotFoundException
     */
    public Class loadClassToProcess(byte[] byteArray, String name) throws ClassNotFoundException {

        Class loadingClass = defineClass(name, byteArray, 0, byteArray.length);
        if(loadingClass == null){
            throw new ClassNotFoundException("Error in loading " + name);
        }
        resolveClass(loadingClass);
        log.info("Successfully loaded class " + name);

    return loadingClass;
    }

}
