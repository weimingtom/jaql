/*
 * Copyright (C) IBM Corp. 2009.
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
package com.ibm.jaql.lang.expr.path;

import com.ibm.jaql.json.type.JsonString;
import com.ibm.jaql.json.type.JsonValue;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.expr.core.Expr;


/** A referenced field in a path step for records */
public abstract class PathFields extends PathStep
{

  /**
   * @param exprs
   */
  public PathFields(Expr[] exprs)
  {
    super(exprs);
  }

  /**
   * @param name
   */
  public PathFields(Expr next)
  {
    super(next);
  }

  /**
   * @param expr0
   * @param expr1
   */
  public PathFields(Expr name, Expr next)
  {
    super(name, next);
  }


  /* (non-Javadoc)
   * @see com.ibm.jaql.lang.expr.core.PathExpr#eval(com.ibm.jaql.lang.core.Context)
   */
  @Override
  public JsonValue eval(Context context) throws Exception
  {
    throw new RuntimeException("PathFields should not be evaluated!");
  }

  public abstract boolean matches(Context context, JsonString name) throws Exception;
  

}
