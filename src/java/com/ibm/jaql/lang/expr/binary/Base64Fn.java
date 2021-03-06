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
package com.ibm.jaql.lang.expr.binary;


import org.apache.commons.codec.binary.Base64;

import com.ibm.jaql.json.type.JsonBinary;
import com.ibm.jaql.json.type.JsonString;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.function.DefaultBuiltInFunctionDescriptor;

/**
 *  @jaqlDescription Convert an ascii/utf8 base64 string into a binary string.
 *  
 *  Usage:
 *  binary base64(string str)
 *  
 *  @jaqlExample base64("utf8string");
 *  hex('BAD7FCB2DAE20000')
 */
public class Base64Fn extends Expr
{
  public static class Descriptor extends DefaultBuiltInFunctionDescriptor.Par11
  {
    public Descriptor()
    {
      super("base64", Base64Fn.class);
    }
  }
  
  protected Base64 codec = new Base64();

  /**
   * @param exprs
   */
  public Base64Fn(Expr[] exprs)
  {
    super(exprs);
  }

  /**
   * @param expr0
   */
  public Base64Fn(Expr expr0)
  {
    super(expr0);
  }

  /* (non-Javadoc)
   * @see com.ibm.jaql.lang.expr.core.Expr#eval(com.ibm.jaql.lang.core.Context)
   */
  @Override
  public JsonBinary eval(Context context) throws Exception
  {
    JsonString jstr = (JsonString)exprs[0].eval(context);
    if( jstr == null )
    {
      return null;
    }

    // TODO: use a better codec that allows us to pass in the length,
    // and preferably an output buffer
    int len = jstr.bytesLength();
    byte[] utf8 = new byte[len];
    jstr.writeBytes(utf8);
    byte[] base64 = codec.decode(utf8);
    return new JsonBinary(base64);
  }
}
