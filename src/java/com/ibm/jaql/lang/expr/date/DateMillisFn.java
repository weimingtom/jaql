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
package com.ibm.jaql.lang.expr.date;

import java.util.Map;

import com.ibm.jaql.json.type.JsonDate;
import com.ibm.jaql.json.type.JsonLong;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.core.ExprProperty;
import com.ibm.jaql.lang.expr.function.DefaultBuiltInFunctionDescriptor;

/**
 * 
 * @jaqlDescription Represent the date using milliseconds.
 * 
 * Usage:
 * long dateMillis(date d)
 * 
 * the argument is restricted with date type, or it causes bad casting exception. 
 * 
 * @jaqlExample dateMillis(date('2000-01-01T12:00:00Z'));
 * 946728000000
 */
public class DateMillisFn extends Expr
{
  public static class Descriptor extends DefaultBuiltInFunctionDescriptor.Par11
  {
    public Descriptor()
    {
      super("dateMillis", DateMillisFn.class);
    }
  }
  
  public DateMillisFn(Expr[] exprs)
  {
    super(exprs);
  }

  @Override
  public Map<ExprProperty, Boolean> getProperties() 
  {
    Map<ExprProperty, Boolean> result = super.getProperties();
    result.put(ExprProperty.ALLOW_COMPILE_TIME_COMPUTATION, true);
    return result;
  }
  
  @Override
  public JsonLong eval(Context context) throws Exception
  {
    JsonDate d = (JsonDate)exprs[0].eval(context);
    if( d == null )
    {
      return null;
    }
    JsonLong m = new JsonLong(d.get()); // TODO: memory
    return m;
  }

}
