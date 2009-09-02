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
package com.ibm.jaql.lang.registry;

import com.ibm.jaql.json.type.JsonString;
import com.ibm.jaql.json.type.JsonValue;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.function.DefaultBuiltInFunctionDescriptor;
import com.ibm.jaql.lang.util.JaqlUtil;

/**
 * 
 */
public class RegisterFunctionExpr extends Expr
{
  public static class Descriptor extends DefaultBuiltInFunctionDescriptor.Par22
  {
    public Descriptor()
    {
      super("registerFunction", RegisterFunctionExpr.class);
    }
  }
  
  /**
   * @param exprs
   */
  public RegisterFunctionExpr(Expr[] exprs)
  {
    super(exprs);
  }

  public RegisterFunctionExpr(Expr jaqlName, Expr javaName)
  {
    super(jaqlName, javaName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.Expr#eval(com.ibm.jaql.lang.core.Context)
   */
  @Override
  public JsonValue eval(Context context) throws Exception
  {
    JsonString fnName = JaqlUtil.enforceNonNull((JsonString)exprs[0].eval(context));
    JsonString className = JaqlUtil.enforceNonNull((JsonString) exprs[1].eval(context));
    JaqlUtil.getFunctionStore().register(fnName, className);
    return fnName;
  }
}