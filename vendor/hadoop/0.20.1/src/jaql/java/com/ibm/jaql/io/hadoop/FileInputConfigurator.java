/*
 * Copyright (C) IBM Corp. 2008.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.jaql.io.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobConf;

import com.ibm.jaql.io.AdapterStore;
import com.ibm.jaql.json.type.JsonRecord;
import com.ibm.jaql.json.type.JsonValue;

/**
 * A configurator that specifically writes the JobConf for a given
 * FileInputFormat
 */
public class FileInputConfigurator implements InitializableConfSetter
{
  protected String location;

  public void init(JsonValue options) throws Exception
  {
    location = AdapterStore.getStore().getLocation((JsonRecord) options);
  }

  protected void registerSerializers(JobConf conf)
  {
    HadoopSerializationDefault.register(conf);
  }
  
  public void setSequential(JobConf conf) throws Exception
  {
    set(conf);    
  }

  public void setParallel(JobConf conf) throws Exception
  {
    set(conf);    
  }

  /**
   * @param conf
   * @throws Exception
   */
  protected void set(JobConf conf) throws Exception
  {
    FileInputFormat.setInputPaths(conf, new Path(location));
    registerSerializers(conf);
  }
}
