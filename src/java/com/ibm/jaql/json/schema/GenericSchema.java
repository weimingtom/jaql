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
package com.ibm.jaql.json.schema;

import com.ibm.jaql.json.type.JsonRecord;
import com.ibm.jaql.json.type.JsonType;
import com.ibm.jaql.json.type.JsonValue;
import com.ibm.jaql.lang.expr.core.Parameters;
import com.ibm.jaql.lang.util.JaqlUtil;
import com.ibm.jaql.util.Bool3;

/** Generic schema used for types that do not have parameters */
public class GenericSchema extends Schema
{
  JsonType type;

  // -- schema parameters -------------------------------------------------------------------------
  
  private static Parameters parameters = null; 
  
  public static Parameters getParameters()
  {
    if (parameters == null)
    {
      parameters = new Parameters(); // no args
    }
    return parameters;
  }
  
  
  // -- construction ------------------------------------------------------------------------------
  
  public GenericSchema(JsonType type, JsonRecord args)
  {
    this(type);
  }
  
  public GenericSchema(JsonType type)
  {
    JaqlUtil.enforceNonNull(type);
    this.type = type;
  }

  
  // -- Schema methods ----------------------------------------------------------------------------

  @Override
  public SchemaType getSchemaType()
  {
    return SchemaType.GENERIC;
  }

  @Override
  public Bool3 isNull()
  {
    return Bool3.FALSE;
  }

  @Override
  public Bool3 isConst()
  {
    return Bool3.UNKNOWN;
  }

  @Override
  public Bool3 isArray()
  {
    return Bool3.FALSE;
  }

  @Override
  public boolean matches(JsonValue value) throws Exception
  {
    return type.clazz.isInstance(value);
  }

  
  // -- getters -----------------------------------------------------------------------------------
  
  public JsonType getType()
  {
    return type;  
  }
}